package com.bawp.bandme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bawp.bandme.call_back_interface.CallBack_FireBaseDatabase;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationInstruments;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationLoginInfo;
import com.bawp.bandme.model.BandMeProfile;
import com.bawp.bandme.R;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationPersonalData;
import com.bawp.bandme.fragments.Fragment_Instruments;
import com.bawp.bandme.fragments.Fragment_LoginInfo;
import com.bawp.bandme.fragments.Fragment_PersonalData;
import com.bawp.bandme.util.FireBaseMethods;
import java.util.ArrayList;


public class Activity_Register extends AppCompatActivity {


    private int currentStep = 1;

    private Fragment_LoginInfo fragment_loginInfo;
    private Fragment_Instruments fragment_instruments;
    private Fragment_PersonalData fragment_personalData;

    private ImageView register_IMG_secondStep;
    private ImageView register_IMG_thirdStep;
    private BandMeProfile bandMeProfile;

    private String userEmail;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findAllViewById();

        bandMeProfile = new BandMeProfile();
        //fragments
        initFragments();
        addFragments();

        //fireBase
        FireBaseMethods.getInstance().setCallBack_fireBaseDatabase(callBack_fireBaseDatabase);
    }


    /*
    call Back first page
     */
    CallBack_RegistrationLoginInfo callBack_registrationLoginInfo = new CallBack_RegistrationLoginInfo() {

        @Override
        public void advanceLoginInfoStep(String email, String password, String validatePassword) {

            //get email and password from the user input
            userEmail = email;
            userPassword = password;
            //move to next activity
            currentStep = 2;
            //change image drawable to show advancement
            register_IMG_secondStep.setImageResource(R.drawable.progress_on);
            changeFragment();
        }
    };


     /*
    call Back second page
     */
    CallBack_RegistrationInstruments callBack_registrationInstruments = new CallBack_RegistrationInstruments() {
        @Override
        public void advanceInstrumentStep(ArrayList<String> instruments) {
            //add email and password to the account creation
            bandMeProfile.setInstruments(instruments);

            //move to next activity
            currentStep = 3;
            changeFragment();
            register_IMG_thirdStep.setImageResource(R.drawable.progress_on);
        }

        @Override
        public void returnTOLoginInfo() {
            currentStep = 1;
            changeFragment();
            register_IMG_secondStep.setImageResource(R.drawable.progress_off);
        }
    };

    /*
   call Back third page
    */
    CallBack_RegistrationPersonalData callBack_registrationPersonalData = new CallBack_RegistrationPersonalData() {

        @Override
        public void createAnAccount(String firstName, String lastName, String age, String info, String district) {
            //add first name, last name, age and personal info to the account creation
            Log.d("jjjj", "activity register: + district");
            bandMeProfile.setFirstName(firstName).setLastName(lastName).setAge(age).setSelfInfo(info).setDistrict(district);
            addBandMeProfileToFireBase();
        }

        @Override
        public void backToRegisterInstruments() {
            currentStep = 2;
            changeFragment();
            register_IMG_secondStep.setImageResource(R.drawable.progress_on);
        }
    };

    //CallBackFireBase
    //after firebase created the user and saved the user info it will close this activity
    CallBack_FireBaseDatabase callBack_fireBaseDatabase = new CallBack_FireBaseDatabase() {

        @Override
        public void finishedAccountCreation() {
            finish();
        }
    };

    private void addBandMeProfileToFireBase() {
        FireBaseMethods.getInstance().createNewAccount(this, userEmail, userPassword, bandMeProfile);
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
        fragment_loginInfo = Fragment_LoginInfo.newInstance();
        fragment_loginInfo.setActivityCallBack(callBack_registrationLoginInfo);

        fragment_instruments = Fragment_Instruments.newInstance();
        fragment_instruments.setActivityCallBack(callBack_registrationInstruments);

        fragment_personalData = Fragment_PersonalData.newInstance();
        fragment_personalData.setActivityCallBack(callBack_registrationPersonalData);
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
        //images
        register_IMG_secondStep = findViewById(R.id.register_IMG_secondStep);
        register_IMG_thirdStep = findViewById(R.id.register_IMG_thirdStep);

    }

}