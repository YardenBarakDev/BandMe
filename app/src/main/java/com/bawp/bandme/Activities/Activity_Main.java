package com.bawp.bandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.bawp.bandme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_Main extends AppCompatActivity {

    private BottomNavigationView Register_BTN_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register_BTN_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        //  getSupportFragmentManager().beginTransaction().
        //                replace(R.id.Register_LAY_register, fragment_loginInfo).commit();
    }


    private void findAllViewById() {
        Register_BTN_navigation = findViewById(R.id.Register_BTN_navigation);
    }
    /*
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.NAV_LoginInfo:
                            selectedFragment = fragment_loginInfo;
                            break;
                        case R.id.NAV_Instruments:
                            selectedFragment = fragment_instruments;
                            break;
                        case R.id.NAV_PersonalData:
                            selectedFragment = fragment_personalData;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.Register_LAY_register, selectedFragment).commit();

                    return true;
                }
            };

     */
}