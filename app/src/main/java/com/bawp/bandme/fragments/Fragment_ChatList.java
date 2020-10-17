package com.bawp.bandme.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawp.bandme.Activities.Activity_Chat;
import com.bawp.bandme.Activities.Activity_DifferentUserProfile;
import com.bawp.bandme.R;
import com.bawp.bandme.adapters.ChatListAdapter;
import com.bawp.bandme.model.BandMeContact;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MyUtil;
import com.bawp.bandme.util.StringDateComparator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Fragment_ChatList extends Fragment {

    protected View view;
    private RecyclerView ChatList_LST_musicians;
    ArrayList<BandMeContact> contacts;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        }
        contacts = new ArrayList<>();
        findView();
        fetchContactsFromFirebase();
        return view;
    }

    public static Fragment_ChatList newInstance(){
        return new Fragment_ChatList();
    }

    private void findView() {
        ChatList_LST_musicians = view.findViewById(R.id.ChatList_LST_musicians);
    }

    private void setRecyclerViewAdapter() {
        ChatListAdapter chatListAdapter= new ChatListAdapter(getActivity(), contacts);
        chatListAdapter.setClickListener(chatListClickListener);
        ChatList_LST_musicians.setLayoutManager(new LinearLayoutManager(getActivity()));
        ChatList_LST_musicians.setAdapter(chatListAdapter);
    }

    //get all contacts the user has chat with
    private void fetchContactsFromFirebase() {

        DatabaseReference referenceForCurrentUser = FirebaseDatabase.getInstance().
                getReference(FireBaseMethods.KEYS.USER).
                child(FireBaseMethods.getInstance().getmAuth().getCurrentUser().getUid()).
                child(FireBaseMethods.KEYS.CONTACTS);

        referenceForCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contacts.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    BandMeContact chatID = ds.getValue(BandMeContact.class);
                    if (chatID != null && chatID.isActive()){
                        contacts.add(chatID);
                    }
                }
                if (contacts.size() > 0){
                    Collections.sort(contacts, new StringDateComparator());
                    setRecyclerViewAdapter();
                }
            }
            //in case the server is unable to bring the data
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    ChatListAdapter.chatListClickListener chatListClickListener = new ChatListAdapter.chatListClickListener() {

        @Override
        public void getProfile(final BandMeContact bandMeContact) {

            /* FireBaseMethods.getInstance().getMyRef().child(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener()

             */
            FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.USER).child(bandMeContact.getParticipant()).
                    addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            BandMeProfile bandMeProfile = snapshot.getValue(BandMeProfile.class);
                            if (bandMeProfile != null){
                                chatListClickListener.moveToChatActivity(bandMeContact, bandMeProfile);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }});
        }

        @Override
        public void moveToChatActivity(BandMeContact bandMeContact, BandMeProfile bandMeProfile) {
            Intent intent = new Intent(getActivity(), Activity_Chat.class);
            intent.putExtra(MyUtil.KEYS.BAND_ME_PROFILE, bandMeProfile);
            intent.putExtra(FireBaseMethods.KEYS.KEY, bandMeContact.getChatId());
            startActivity(intent);
        }
    };
}