package com.bawp.bandme.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.bawp.bandme.Activities.Activity_DifferentUserProfile;
import com.bawp.bandme.adapters.MyRecyclerViewAdapter;
import com.bawp.bandme.R;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.model.BandMeUID;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.MyUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

public class Fragment_SearchMusicians extends Fragment {

    protected View view;
    private RecyclerView SearchMusicians_LST_musicians;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private AppCompatAutoCompleteTextView SearchMusicians_LST_musicians_Spinner_district;
    private AppCompatAutoCompleteTextView SearchMusicians_LST_musicians_Spinner_instruments;

    private String chosenDistrict = "";
    private String chosenInstrument = "";
    private ArrayList<BandMeProfile> bandMeProfiles;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_search_musicians, container, false);
        }
        findView();
        setSpinner();
        bandMeProfiles = new ArrayList<>();
        getAllUsersFromFireBase();
        getValuesFromSpinners();
        return view;
    }

    private void getValuesFromSpinners() {

        SearchMusicians_LST_musicians_Spinner_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenDistrict = (String) adapterView.getItemAtPosition(i);
                chooseSort();
            }
        });

        SearchMusicians_LST_musicians_Spinner_instruments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenInstrument = (String) adapterView.getItemAtPosition(i);
                chooseSort();
            }
        });
    }

    private void setSpinner() {
       if (getActivity() != null){
           SearchMusicians_LST_musicians_Spinner_instruments.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, MyUtil.Arrays.instruments));
           SearchMusicians_LST_musicians_Spinner_district.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, MyUtil.Arrays.districts));

       }
    }

    private void setRecyclerViewAdapter() {
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), bandMeProfiles);
        myRecyclerViewAdapter.setClickListener(profileListClickListener);
        SearchMusicians_LST_musicians.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchMusicians_LST_musicians.setAdapter(myRecyclerViewAdapter);
    }



    public static Fragment_SearchMusicians newInstance(){
        return new Fragment_SearchMusicians();
    }


    private void chooseSort(){

        if (chosenInstrument.equals("All") && chosenDistrict.equals("All") || chosenInstrument.equals("All") && chosenDistrict.equals("")
            || chosenInstrument.equals("") && chosenDistrict.equals("All") || chosenInstrument.equals("") && chosenDistrict.equals("")){
            getAllUsersFromFireBase();
        }
        else if (chosenInstrument.equals("All") || chosenInstrument.equals(""))
            sortByDistrict();
        else if (chosenDistrict.equals("All") || chosenDistrict.equals(""))
            sortByInstrument();
        else
            sortByDistrictAndInstrument();
    }
    private void sortByDistrictAndInstrument() {
        bandMeProfiles.clear();
        setRecyclerViewAdapter();
        FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS_AND_INSTRUMENTS)
                .child(chosenDistrict).child(chosenInstrument).
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            BandMeUID bandMeUID = ds.getValue(BandMeUID.class);
                            if (bandMeUID != null ){
                                getUserFromID(bandMeUID.getUid());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sortByInstrument() {
        bandMeProfiles.clear();
        setRecyclerViewAdapter();
        FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.ALL_INSTRUMENTS).
                child(chosenInstrument).
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                           BandMeUID bandMeUID = ds.getValue(BandMeUID.class);

                            //make sure the current user won't be in the search list
                            if (bandMeUID != null ){
                                //add user to array
                                getUserFromID(bandMeUID.getUid());
                                //bandMeProfiles.add(profile);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sortByDistrict() {
        bandMeProfiles.clear();
        setRecyclerViewAdapter();
        FireBaseMethods.getInstance().getDatabase().getReference().child(FireBaseMethods.KEYS.DISTRICTS)
                .child(chosenDistrict).
                addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    BandMeUID bandMeUID = ds.getValue(BandMeUID.class);

                    //make sure the current user won't be in the search list
                    if (bandMeUID != null ){
                        //add user to array
                        getUserFromID(bandMeUID.getUid());
                        //bandMeProfiles.add(profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //get the list of all the users from firebase and present them in the recycler view
    private void getAllUsersFromFireBase() {

        FireBaseMethods.getInstance().getMyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bandMeProfiles.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    BandMeProfile profile = ds.getValue(BandMeProfile.class);

                    //make sure the current user won't be in the search list
                    if (profile != null && !profile.getUid().equals(
                            Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getCurrentUser()).getUid())){

                        //add user to array
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

    private void getUserFromID(String id) {
        FireBaseMethods.getInstance().getMyRef().child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BandMeProfile bandMeProfile = snapshot.getValue(BandMeProfile.class);
                        if (bandMeProfile != null && !bandMeProfile.getUid().equals(Objects.requireNonNull(FireBaseMethods.getInstance().getmAuth().getCurrentUser()).getUid())) {
                            bandMeProfiles.add(bandMeProfile);
                        }
                        if (bandMeProfiles.size() > 0)
                            setRecyclerViewAdapter();
                    }

                    //in case the server is unable to bring the data
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    //once the user click on one of the cards, it will open an activity with the user data "Activity_DifferentUserProfile"

    MyRecyclerViewAdapter.ProfileListClickListener profileListClickListener= new MyRecyclerViewAdapter.ProfileListClickListener() {
        @Override
        public void getProfile(BandMeProfile bandMeProfile) {
            Intent intent = new Intent(getActivity(), Activity_DifferentUserProfile.class);
            intent.putExtra(MyUtil.KEYS.BAND_ME_PROFILE, bandMeProfile);
            startActivity(intent);
        }
    };

    private void findView() {
        SearchMusicians_LST_musicians = view.findViewById(R.id.SearchMusicians_LST_musicians);
        SearchMusicians_LST_musicians_Spinner_district = view.findViewById(R.id.SearchMusicians_LST_musicians_Spinner_district);
        SearchMusicians_LST_musicians_Spinner_instruments = view.findViewById(R.id.SearchMusicians_LST_musicians_Spinner_instruments);

    }
}