package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bawp.bandme.R;
import com.bawp.bandme.adapters.MyChatAdapter;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.model.Chat;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MyUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Activity_Chat extends AppCompatActivity {

    private ImageView Chat_IMAGE_profile;
    private ImageView Chat_IMAGE_return;
    private TextView Chat_LBL_name;

    private ImageButton Chat_BTN_send;
    private EditText Chat_EditText_message;
    private BandMeProfile OtherUserProfileProfile;

    private RecyclerView Chat_RecyclerView_messages;
    private ArrayList<Chat> chatsList;

    private FirebaseUser firebaseUser;
    private String chatKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findViews();
        initRecycleView();

        //get the profile from Fragment_searchMusicians
        OtherUserProfileProfile = (BandMeProfile)getIntent().getSerializableExtra(MyUtil.KEYS.BAND_ME_PROFILE);
        if (OtherUserProfileProfile!= null){
            showUserDetails(OtherUserProfileProfile);
        }
        //get conversation key
        chatKey = getIntent().getStringExtra(FireBaseMethods.KEYS.KEY);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        readMessages();
        //messageListener();

        //listeners
        Chat_BTN_send.setOnClickListener(ChatOnClickListener);
        Chat_IMAGE_return.setOnClickListener(ChatOnClickListener);
    }


    private void readMessages(){

        chatsList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference(FireBaseMethods.KEYS.CHAT).
                child(chatKey);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Chat chat = ds.getValue(Chat.class);
                    if (chat != null){
                        chatsList.add(chat);
                    }
                }
                MyChatAdapter myChatAdapter = new MyChatAdapter(Activity_Chat.this, chatsList);
                Chat_RecyclerView_messages.setAdapter(myChatAdapter);

                //scroll down automatically whenever a new message is send
                if (chatsList.size() >= 1){
                    Chat_RecyclerView_messages.smoothScrollToPosition(chatsList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });
    }


    //send the message if it is not empty
    View.OnClickListener ChatOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch ((String)view.getTag()){
                case "Chat_BTN_send":
                    String message = Chat_EditText_message.getText().toString();
                    if (!message.trim().equals("")){
                        sendMessage(firebaseUser.getUid(), OtherUserProfileProfile.getUid(), message);
                        updateContact(firebaseUser.getUid(), OtherUserProfileProfile.getUid(), message);
                    }
                    Chat_EditText_message.setText("");
                    break;

                case "Chat_IMAGE_return":
                    finish();
                    break;
            }
        }
    };

    private void updateContact(String sender, String receiver, String message) {
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        //update current user
        FireBaseMethods.getInstance().getMyRef().child(sender).child(FireBaseMethods.KEYS.CONTACTS).child(receiver).child("active").setValue(true);
        FireBaseMethods.getInstance().getMyRef().child(sender).child(FireBaseMethods.KEYS.CONTACTS).child(receiver).child("lastUpdate").setValue(currentDateTimeString);
        FireBaseMethods.getInstance().getMyRef().child(sender).child(FireBaseMethods.KEYS.CONTACTS).child(receiver).child("lastMessage").setValue(message);

        //update other user
        FireBaseMethods.getInstance().getMyRef().child(receiver).child(FireBaseMethods.KEYS.CONTACTS).child(sender).child("active").setValue(true);
        FireBaseMethods.getInstance().getMyRef().child(receiver).child(FireBaseMethods.KEYS.CONTACTS).child(sender).child("lastUpdate").setValue(currentDateTimeString);
        FireBaseMethods.getInstance().getMyRef().child(receiver).child(FireBaseMethods.KEYS.CONTACTS).child(sender).child("lastMessage").setValue(message);
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference(FireBaseMethods.KEYS.CHAT).
                child(chatKey);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(MyUtil.KEYS.SENDER, sender);
        hashMap.put(MyUtil.KEYS.RECEIVER, receiver);
        hashMap.put(MyUtil.KEYS.MESSAGE, message);

        databaseReference.push().setValue(hashMap);
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
        Chat_IMAGE_return = findViewById(R.id.Chat_IMAGE_return);
        Chat_LBL_name = findViewById(R.id.Chat_LBL_name);
        Chat_RecyclerView_messages = findViewById(R.id.Chat_RecyclerView_messages);
        Chat_BTN_send = findViewById(R.id.Chat_BTN_send);
        Chat_EditText_message = findViewById(R.id.Chat_EditText_message);
        Chat_RecyclerView_messages = findViewById(R.id.Chat_RecyclerView_messages);
    }

}