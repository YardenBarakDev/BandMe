package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bawp.bandme.R;
import com.bawp.bandme.call_back_interface.CallBack_ChatExist;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.model.Chat;
import com.bawp.bandme.model.ChatID;
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



public class Activity_DifferentUserProfile extends AppCompatActivity {

    private ImageView OtherUserProfile_IMAGE_background;
    private ImageView OtherUserProfile_IMAGE_profilePicture;
    private ImageView OtherUserProfile_IMAGE_sendMessage;
    private TextView OtherUserProfile_LBL_firstName;
    private TextView OtherUserProfile_LBL_lastName;
    private TextView OtherUserProfile_LBL_instruments;
    private TextView OtherUserProfile_LBL_age;
    private TextView OtherUserProfile_LBL_info;

    //callback
    private CallBack_ChatExist myCallBack_ChatExist;

    private BandMeProfile bandMeProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__different_user_profile);

        findView();
        myCallBack_ChatExist = callBack_chatExist;
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
            checkIfUsersTalked();
        }
    };

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

    private void checkIfUsersTalked(){
        Log.d("jjjj", "checkIfUsersTalked");

        //database/User/CurrentUserID/Contacts/participant
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference(FireBaseMethods.KEYS.USER).
                child(FireBaseMethods.getInstance().getmAuth().getCurrentUser().getUid()).
                child(FireBaseMethods.KEYS.CONTACTS);

        //check if the the user has a previous chat with the current user
        databaseReference.orderByChild(FireBaseMethods.KEYS.PARTICIPANT).equalTo(bandMeProfile.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                                ChatID chatID = userSnapshot.getValue(ChatID.class);
                                //users has a previous chat
                                Log.d("jjjj", "checkIfUsersTalked:  talked");

                                if (chatID != null){
                                    String key = chatID.getChatId();
                                    Log.d("jjjj", "checkIfUsersTalked: key = " + key + chatID.toString());
                                    myCallBack_ChatExist.hasPreviousConversations(key);
                                }
                            }
                        }
                        else{
                            //users don't have a precious chat
                            Log.d("jjjj", "checkIfUsersTalked:didn't talked");
                            callBack_chatExist.noPreviousConversations();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void createNewChatIdAndMoveToChat() {

        //create empty chat to set under chat id
        //Chat chat = new Chat();
        //push the chat to create and empty instance in chatID that will be the place where the messages between the users will be stored
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference(FireBaseMethods.KEYS.CHAT).push();

        //get the unique key off the conversation
        String key = chatReference.getKey();
        //chatReference.setValue(chat);

        //Update current user participants

        ChatID chatIdForCurrentUser = new ChatID(key, bandMeProfile.getUid());

        DatabaseReference referenceForCurrentUser = FirebaseDatabase.getInstance().
                getReference(FireBaseMethods.KEYS.USER).
                child(FireBaseMethods.getInstance().getmAuth().getCurrentUser().getUid()).
                child(FireBaseMethods.KEYS.CONTACTS);

        referenceForCurrentUser.push().setValue(chatIdForCurrentUser);


        //update other user participants
        ChatID chatIdForOtherUser = new ChatID(key, FireBaseMethods.getInstance().getmAuth().getCurrentUser().getUid());

        //database/User/other contact id/contacts
        DatabaseReference referenceForOtherUser = FirebaseDatabase.getInstance().
                getReference(FireBaseMethods.KEYS.USER).
                child(bandMeProfile.getUid()).
                child(FireBaseMethods.KEYS.CONTACTS);

        referenceForOtherUser.push().setValue(chatIdForOtherUser);

        //move to chat activity
        Intent intent = new Intent(Activity_DifferentUserProfile.this , Activity_Chat.class);
        intent.putExtra(MySP.KEYS.BAND_ME_PROFILE, bandMeProfile);
        intent.putExtra(FireBaseMethods.KEYS.KEY, key);
        startActivity(intent);


    }


    private void moveToChat(String key) {

        //move to chat activity
        Intent intent = new Intent(Activity_DifferentUserProfile.this , Activity_Chat.class);
        intent.putExtra(MySP.KEYS.BAND_ME_PROFILE, bandMeProfile);
        intent.putExtra(FireBaseMethods.KEYS.KEY, key);
        startActivity(intent);
    }



    CallBack_ChatExist callBack_chatExist = new CallBack_ChatExist() {

        @Override
        public void noPreviousConversations() {
            createNewChatIdAndMoveToChat();
            Log.d("jjjj", "noPreviousConversations: ");
        }

        @Override
        public void hasPreviousConversations(String key) {
            moveToChat(key);
            Log.d("jjjj", "previousConversations: ");
        }
    };
}