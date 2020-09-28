package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bawp.bandme.R;
import com.bawp.bandme.adapters.MyChatAdapter;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.model.Chat;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MySP;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Chat extends AppCompatActivity {

    private ImageView Chat_IMAGE_profile;
    private TextView Chat_LBL_name;

    private ImageButton Chat_BTN_send;
    private EditText Chat_EditText_message;
    private BandMeProfile bandMeProfile;

    private RecyclerView Chat_RecyclerView_messages;
    private ArrayList<Chat> chatsList;

    //firebase
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findViews();
        initRecycleView();

        //get the profile from Fragment_searchMusicians
        bandMeProfile = (BandMeProfile)getIntent().getSerializableExtra(MySP.KEYS.BAND_ME_PROFILE);
        if (bandMeProfile!= null){
            showUserDetails(bandMeProfile);
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        messageListener();
        //button listener
        Chat_BTN_send.setOnClickListener(ChatOnClickListener);

    }

    private void messageListener() {
        //messages listener
        databaseReference = FirebaseDatabase.getInstance().getReference(FireBaseMethods.KEYS.USER).child(FireBaseMethods.getInstance().getmAuth().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessages();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readMessages(){

        chatsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(FireBaseMethods.KEYS.CHAT);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);

                    if (chat.getReceiver().equals(bandMeProfile.getUid()) && chat.getSender().equals(firebaseUser.getUid())
                    || chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(bandMeProfile.getUid())){
                        chatsList.add(chat);
                    }
                    MyChatAdapter myChatAdapter = new MyChatAdapter(Activity_Chat.this, chatsList);
                    Chat_RecyclerView_messages.setAdapter(myChatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //send the message if it is not empty
    View.OnClickListener ChatOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String message = Chat_EditText_message.getText().toString();
            if (!message.trim().equals("")){
                sendMessage(firebaseUser.getUid(), bandMeProfile.getUid(), message);
            }
            Chat_EditText_message.setText("");
        }
    };

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(MySP.KEYS.SENDER, sender);
        hashMap.put(MySP.KEYS.RECEIVER, receiver);
        hashMap.put(MySP.KEYS.MESSAGE, message);

        databaseReference.child(FireBaseMethods.KEYS.CHAT).push().setValue(hashMap);
    }


    private void initRecycleView() {
        Chat_RecyclerView_messages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        Chat_RecyclerView_messages.setLayoutManager(linearLayoutManager);
    }




    private void showUserDetails(BandMeProfile bandMeProfile) {
        //set image
        if (!bandMeProfile.getImageUrl().equals("")){
            Glide.with(this)
                    .load(bandMeProfile.getImageUrl())
                    .into(Chat_IMAGE_profile);
        }
        else{
            Chat_IMAGE_profile.setImageResource(R.drawable.profile);
        }

        //set name
        String userFullName = bandMeProfile.getFirstName() +"\n" + bandMeProfile.getLastName();
        Chat_LBL_name.setText(userFullName);
    }


    private void findViews() {

        Chat_IMAGE_profile = findViewById(R.id.Chat_IMAGE_profile);
        Chat_LBL_name = findViewById(R.id.Chat_LBL_name);
        Chat_RecyclerView_messages = findViewById(R.id.Chat_RecyclerView_messages);
        Chat_BTN_send = findViewById(R.id.Chat_BTN_send);
        Chat_EditText_message = findViewById(R.id.Chat_EditText_message);
        Chat_RecyclerView_messages = findViewById(R.id.Chat_RecyclerView_messages);
    }

}