package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bawp.bandme.R;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MySP;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Activity_DifferentUserProfile extends AppCompatActivity {

    private ImageView OtherUserProfile_IMAGE_background;
    private ImageView OtherUserProfile_IMAGE_profilePicture;
    private ImageView OtherUserProfile_IMAGE_sendMessage;
    private TextView OtherUserProfile_LBL_firstName;
    private TextView OtherUserProfile_LBL_lastName;
    private TextView OtherUserProfile_LBL_instruments;
    private TextView OtherUserProfile_LBL_age;
    private TextView OtherUserProfile_LBL_info;

    private BandMeProfile bandMeProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__different_user_profile);

        findView();

        //get the profile from Fragment_searchMusicians
        bandMeProfile = (BandMeProfile)getIntent().getSerializableExtra(MySP.KEYS.BAND_ME_PROFILE);
        if (bandMeProfile!= null){

            showUserInfo(bandMeProfile);
            setProfilePicture(bandMeProfile.getImageUrl());
        }

        OtherUserProfile_IMAGE_sendMessage.setOnClickListener(differentUserProfileListener);

    }

    View.OnClickListener differentUserProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            moveToChat();
        }
    };

    private void moveToChat() {

        //get unique key for the chat
        String chatKey = FirebaseDatabase.getInstance().getReference().child(FireBaseMethods.KEYS.CHAT).getKey();
       // FirebaseDatabase.getInstance().getReference().child(FireBaseMethods.KEYS.CHAT).child(chatKey).setValue()

        //move to chat activity
        Intent intent = new Intent(Activity_DifferentUserProfile.this , Activity_Chat.class);
        intent.putExtra(MySP.KEYS.BAND_ME_PROFILE, bandMeProfile);
        startActivity(intent);
    }

    private void showUserInfo(BandMeProfile bandMeProfile) {
        //set user data (first name, last name etc) in the relevant places
        OtherUserProfile_LBL_firstName.setText(bandMeProfile.getFirstName());
        OtherUserProfile_LBL_lastName.setText(bandMeProfile.getLastName());
        OtherUserProfile_LBL_age.setText(bandMeProfile.getAge());
        OtherUserProfile_LBL_info.setText(bandMeProfile.getSelfInfo());

        //take care of the instruments arrayList items
        //organize all the items in a string
        if (bandMeProfile.getInstruments().size() > 0){
            StringBuilder instruments = new StringBuilder();
            for (int i = 0; i < bandMeProfile.getInstruments().size(); i++) {
                if (i == bandMeProfile.getInstruments().size() -1)
                    instruments.append(bandMeProfile.getInstruments().get(i));
                else
                    instruments.append(bandMeProfile.getInstruments().get(i)).append(", ");
            }
            OtherUserProfile_LBL_instruments.setText(instruments);
        }
        else{
            OtherUserProfile_LBL_instruments.setText("");
        }
    }

    private void setProfilePicture(String url){
        if (!url.equals("")){
            Glide.with(this)
                    .load(url)
                    .into(OtherUserProfile_IMAGE_profilePicture);
        }
        else{
            OtherUserProfile_IMAGE_profilePicture.setImageResource(R.drawable.profile);
        }
    }


    private void findView() {
        //images
        OtherUserProfile_IMAGE_background = findViewById(R.id.OtherUserProfile_IMAGE_background);
        OtherUserProfile_IMAGE_profilePicture = findViewById(R.id.OtherUserProfile_IMAGE_profilePicture);
        OtherUserProfile_IMAGE_sendMessage = findViewById(R.id.OtherUserProfile_IMAGE_sendMessage);
        //textViews
        OtherUserProfile_LBL_firstName = findViewById(R.id.OtherUserProfile_LBL_firstName);
        OtherUserProfile_LBL_lastName = findViewById(R.id.OtherUserProfile_LBL_lastName);
        OtherUserProfile_LBL_instruments = findViewById(R.id.OtherUserProfile_LBL_instruments);
        OtherUserProfile_LBL_age = findViewById(R.id.OtherUserProfile_LBL_age);
        OtherUserProfile_LBL_info = findViewById(R.id.OtherUserProfile_LBL_info);

    }

    private void checkIfHasConversation(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(FireBaseMethods.KEYS.CHAT);

        FireBaseMethods.getInstance().getMyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    BandMeProfile profile = ds.getValue(BandMeProfile.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}