package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.bawp.bandme.R;
import com.bawp.bandme.fragments.Fragment_SearchMusicians;
import com.bawp.bandme.fragments.Fragment_UserProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Main extends AppCompatActivity {

    private BottomNavigationView Main_BTNVav_navigation;

    private Fragment_UserProfile fragmentUserProfile;
    private Fragment_SearchMusicians fragment_searchMusicians;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find views
        findViewById();

        //fragments
        initfragments();
        addFragments();
        Main_BTNVav_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        Main_BTNVav_navigation.setItemIconTintList(null);

          //getSupportFragmentManager().beginTransaction().
                        //replace(R.id.Main_LAY_fragment, fragmentUserProfile).commit();
    }

    private void initfragments() {
        //profile fragment
        fragmentUserProfile = Fragment_UserProfile.newInstance();
        //fragmentUserProfile.setActivityCallBack();

        //search fragment
        fragment_searchMusicians = Fragment_SearchMusicians.newInstance();
        //fragment_searchMusicians.setActivityCallBack();
    }
    private void addFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //add fragments to the layout
        transaction.add(R.id.Main_LAY_fragment, fragmentUserProfile);
        transaction.add(R.id.Main_LAY_fragment, fragment_searchMusicians);

        //set profile fragment to be the first one
        getSupportFragmentManager().beginTransaction().hide(fragment_searchMusicians).commit();

        //commit
        transaction.commit();
    }

    private void findViewById() {
        Main_BTNVav_navigation = findViewById(R.id.Main_BTNVav_navigation);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.NAV_profile:
                            getSupportFragmentManager().beginTransaction().show(fragmentUserProfile).commit();
                            getSupportFragmentManager().beginTransaction().hide(fragment_searchMusicians).commit();
                            break;
                        case R.id.NAV_search:
                            getSupportFragmentManager().beginTransaction().show(fragment_searchMusicians).commit();
                            getSupportFragmentManager().beginTransaction().hide(fragmentUserProfile).commit();
                            break;

                    }
                    return true;
                }
            };
}