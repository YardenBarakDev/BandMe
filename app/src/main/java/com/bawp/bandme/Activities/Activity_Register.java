package com.bawp.bandme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bawp.bandme.MyBandProfile;
import com.bawp.bandme.R;
import com.bawp.bandme.fragments.Fragment_Instruments;
import com.bawp.bandme.fragments.Fragment_LoginInfo;
import com.bawp.bandme.fragments.Fragment_PersonalData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Register extends AppCompatActivity {


    private final int MAX_STEPS = 3;
    private int currentStep = 1;

    private Fragment_LoginInfo fragment_loginInfo;
    private Fragment_Instruments fragment_instruments;
    private Fragment_PersonalData fragment_personalData;

    private LinearLayout register_LAY_back;
    private LinearLayout register_LAY_next;

    private ImageView register_IMG_firstStep;
    private ImageView register_IMG_secondStep;
    private ImageView register_IMG_thirdStep;

    private MyBandProfile myBandProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);
        findAllViewById();

        myBandProfile = new MyBandProfile();
        //fragments
        initFragments();
        addFragments();

        register_LAY_back.setOnClickListener(registerOnClickListener);
        register_LAY_next.setOnClickListener(registerOnClickListener);

    }


    private void addFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //add fragments to the layout
        transaction.add(R.id.Register_LAY_register, fragment_loginInfo);
        transaction.add(R.id.Register_LAY_register, fragment_instruments);
        transaction.add(R.id.Register_LAY_register, fragment_personalData);

        //hide 2nd and 3rd fragments
        getSupportFragmentManager().beginTransaction().hide(fragment_instruments).commit();
        getSupportFragmentManager().beginTransaction().hide(fragment_personalData).commit();

        //commit
        transaction.commit();
    }

    private void initFragments() {
        fragment_loginInfo = new Fragment_LoginInfo();
        fragment_instruments = new Fragment_Instruments();
        fragment_personalData = new Fragment_PersonalData();
    }

    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch ((String)view.getTag()){
                case "register_LAY_back":
                    if (currentStep - 1 >= 1){
                        currentStep --;
                        changeFragment();
                        showAdvance();
                    }
                    break;

                case "register_LAY_next":
                    if (currentStep + 1 <= MAX_STEPS){
                        currentStep ++;
                        changeFragment();
                        showAdvance();
                    }
                    break;
            }
        }
    };

    private void showAdvance(){
        switch (currentStep){
            case 2:
                register_IMG_secondStep.setImageResource(R.drawable.progress_on);
                break;

            case 3:

                register_IMG_thirdStep.setImageResource(R.drawable.progress_on);
                break;
        }
    }
    private void changeFragment() {
        switch (currentStep){
            case 1:
                getSupportFragmentManager().beginTransaction().show(fragment_loginInfo).commit();
                getSupportFragmentManager().beginTransaction().hide(fragment_instruments).commit();
                getSupportFragmentManager().beginTransaction().hide(fragment_personalData).commit();
                break;

            case 2:
                getSupportFragmentManager().beginTransaction().hide(fragment_loginInfo).commit();
                getSupportFragmentManager().beginTransaction().show(fragment_instruments).commit();
                getSupportFragmentManager().beginTransaction().hide(fragment_personalData).commit();
                break;

            case 3:
                getSupportFragmentManager().beginTransaction().hide(fragment_loginInfo).commit();
                getSupportFragmentManager().beginTransaction().hide(fragment_instruments).commit();
                getSupportFragmentManager().beginTransaction().show(fragment_personalData).commit();
                break;
        }
    }

    private void findAllViewById() {
        //layouts
        register_LAY_back = findViewById(R.id.register_LAY_back);
        register_LAY_next = findViewById(R.id.register_LAY_next);

        //images
        register_IMG_firstStep = findViewById(R.id.register_IMG_firstStep);
        register_IMG_secondStep = findViewById(R.id.register_IMG_secondStep);
        register_IMG_thirdStep = findViewById(R.id.register_IMG_thirdStep);
    }

}