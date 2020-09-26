package com.bawp.bandme.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawp.bandme.MyRecyclerViewAdapter;
import com.bawp.bandme.R;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.util.FireBaseMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_SearchMusicians extends Fragment {

    protected View view;
    private RecyclerView SearchMusicians_LST_musicians;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private ArrayList<BandMeProfile> bandMeProfiles;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_search, container, false);
        }
        findView();
        bandMeProfiles = new ArrayList<>();
        getAllUsersFromFireBase();

        return view;
    }



    private void setRecyclerViewAdapter() {
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), bandMeProfiles);


        SearchMusicians_LST_musicians.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchMusicians_LST_musicians.setAdapter(myRecyclerViewAdapter);
    }

    private void findView() {
        SearchMusicians_LST_musicians = view.findViewById(R.id.SearchMusicians_LST_musicians);
    }

    public static Fragment_SearchMusicians newInstance(){
        return new Fragment_SearchMusicians();
    }

    /*
    public void setActivityCallBack(CallBack_RegistrationLoginInfo callBack_registrationLoginInfo){
        this.callBack_registrationLoginInfo = callBack_registrationLoginInfo;
    }
     */

    private void getAllUsersFromFireBase() {

        FireBaseMethods.getInstance().getMyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bandMeProfiles.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    BandMeProfile profile = ds.getValue(BandMeProfile.class);
                    //add after checking && profile.getUid() != FireBaseMethods.getInstance().getmAuth().getUid()
                    if (profile != null ){
                        bandMeProfiles.add(profile);
                    }
                }
                //send the list to the adapter
                setRecyclerViewAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}