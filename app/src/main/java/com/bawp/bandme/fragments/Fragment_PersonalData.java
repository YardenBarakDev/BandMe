package com.bawp.bandme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import com.bawp.bandme.R;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationPersonalData;
import com.bawp.bandme.util.MyUtil;
import com.bawp.bandme.util.ValidateUserAccountInfo;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Fragment_PersonalData extends Fragment {

    protected View view;
    private CallBack_RegistrationPersonalData callBack_registrationPersonalData;

    private TextInputLayout PersonalData_TF_firstName;
    private TextInputEditText PersonalData_LBL_firstName;

    private TextInputLayout PersonalData_TF_lastName;
    private TextInputEditText PersonalData_LBL_lastName;

    private TextInputLayout PersonalData_TF_age;
    private TextInputEditText PersonalData_LBL_age;

    private TextInputLayout PersonalData_TF_info;
    private TextInputEditText PersonalData_LBL_info;

    private TextInputLayout PersonalData_LAY_district;

    private MaterialButton PersonalData_BTN_Login;
    private ImageView PersonalData_IMAGE_leftArrow;
    private ImageView PersonalData_IMAGE_backGround;
    private AppCompatAutoCompleteTextView PersonalData_Spinner_district;

    private String userDistrict;
    //init util to check user input
    private ValidateUserAccountInfo validateUserAccountInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        }
        //init util to check user input
        validateUserAccountInfo = new ValidateUserAccountInfo();
        findViews();
        setSpinner();
        setListeners();
        glideBackground();

        return view;
    }

    private void setListeners() {
        PersonalData_IMAGE_leftArrow.setOnClickListener(personalDataListener);
        PersonalData_BTN_Login.setOnClickListener(personalDataListener);
        PersonalData_Spinner_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userDistrict = (String) adapterView.getItemAtPosition(i);
                Log.d("jjjj", "district = " + userDistrict);
            }
        });
    }


    public static Fragment_PersonalData newInstance(){
            return new Fragment_PersonalData();
        }

    public void setActivityCallBack(CallBack_RegistrationPersonalData callBack_registrationPersonalData){
        this.callBack_registrationPersonalData = callBack_registrationPersonalData;
    }

    View.OnClickListener personalDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonsClick(view);
        }
    };

    private void buttonsClick(View view) {
        switch (((String) view.getTag())) {
            case "PersonalData_BTN_Login":
                //send the user input to validation, if the input is valid it will
                //send the info to the activity and the activity will create the user profile
                if (validInfo())
                    if (PersonalData_LBL_firstName.getText() != null && PersonalData_LBL_lastName.getText() != null && PersonalData_LBL_age.getText() != null
                    && PersonalData_LBL_info.getText() != null)
                        callBack_registrationPersonalData.createAnAccount(
                                PersonalData_LBL_firstName.getText().toString().trim(),
                                PersonalData_LBL_lastName.getText().toString().trim(),
                                PersonalData_LBL_age.getText().toString().trim(),
                                PersonalData_LBL_info.getText().toString().trim(), userDistrict);
                break;
            case "PersonalData_IMAGE_leftArrow":
                //return to the previous registration page
                callBack_registrationPersonalData.backToRegisterInstruments();
                break;
        }
    }

    //check if the first name, last name and district are valid
    private boolean validInfo() {
        boolean checkFirstName = validFirstName();
        boolean checkLastName = validLastName();
        boolean checkDistrict = validDistrict();
        boolean checkAge = validAge();
        boolean checkInfo = validPrivateInfo();

        return checkFirstName && checkLastName && checkDistrict && checkAge && checkInfo;
    }

    private boolean validPrivateInfo() {
        if (PersonalData_LBL_info.getText() == null)
            return false;
        if (PersonalData_LBL_info.getText().toString().isEmpty()){
            PersonalData_TF_info.setError("Please write a little bit about yourself");
            return false;
        }
        PersonalData_TF_info.setError("");
        return true;
    }

    private boolean validAge() {
        if (PersonalData_LBL_age.getText() == null)
            return false;
        if (PersonalData_LBL_age.getText().toString().isEmpty()){
            PersonalData_TF_age.setError("Mandatory field");
            return false;
        }
        PersonalData_TF_age.setError("");
        return true;
    }

    private boolean validFirstName(){
        //check if null
        if (PersonalData_LBL_firstName.getText() == null)
            return false;

        //check if the user typed more then 1 digit
        else if (PersonalData_LBL_firstName.getText().toString().trim().isEmpty()){
            PersonalData_TF_firstName.setError("Mandatory field");
            return false;
        }
        //check if the name length is longer then 2
        else if (PersonalData_LBL_firstName.getText().toString().trim().length() < 2){
            PersonalData_TF_firstName.setError("Must contain at least two letters");
            return false;
        }
        //check if user only entered letters
        else if (!validateUserAccountInfo.validateNames(PersonalData_LBL_firstName.getText().toString().trim())){
            PersonalData_TF_firstName.setError("Must contain only alphabetical letters");
            return false;
        }
        else{
            PersonalData_TF_firstName.setError("");
            return true;
        }
    }

    private boolean validLastName() {
        //check if null
        if (PersonalData_LBL_lastName.getText() == null)
            return false;

        //check if the user typed more then 1 digit
        else if (PersonalData_LBL_lastName.getText().toString().trim().isEmpty()){
            PersonalData_TF_lastName.setError("Mandatory field");
            return false;
        }
        //check if the name length is longer then 2
        else if (PersonalData_LBL_lastName.getText().toString().trim().length() < 2){
            PersonalData_TF_lastName.setError("Must contain at least two letters");
            return false;
        }
        //check if user only entered letters
        else if (!validateUserAccountInfo.validateNames(PersonalData_LBL_lastName.getText().toString().trim())){
            PersonalData_TF_lastName.setError("Must contain only alphabetical letters");
            return false;
        }
        else{
            PersonalData_TF_lastName.setError("");
            return true;
        }
    }

    private boolean validDistrict() {
        if (userDistrict == null){
            PersonalData_LAY_district.setError("Mandatory field");
            return false;
        }
        else{
            PersonalData_LAY_district.setError("");
            return true;
        }
    }

    private void setSpinner() {
        if (getActivity() != null)
            PersonalData_Spinner_district.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, MyUtil.Arrays.districtsForSpinner));
    }

    private void glideBackground() {
        Glide
                .with(this)
                .load(R.drawable.background_image)
                .into(PersonalData_IMAGE_backGround);
    }


    private void findViews() {
        //text
        PersonalData_TF_firstName = view.findViewById(R.id.PersonalData_TF_firstName);
        PersonalData_LBL_firstName = view.findViewById(R.id.PersonalData_LBL_firstName);

        PersonalData_TF_lastName = view.findViewById(R.id.PersonalData_TF_lastName);
        PersonalData_LBL_lastName = view.findViewById(R.id.PersonalData_LBL_lastName);

        PersonalData_TF_age = view.findViewById(R.id.PersonalData_TF_age);
        PersonalData_LBL_age = view.findViewById(R.id.PersonalData_LBL_age);
        PersonalData_TF_info = view.findViewById(R.id.PersonalData_TF_info);
        PersonalData_LBL_info = view.findViewById(R.id.PersonalData_LBL_info);


        PersonalData_LAY_district = view.findViewById(R.id.PersonalData_LAY_district);
        //button
        PersonalData_BTN_Login = view.findViewById(R.id.PersonalData_BTN_Login);
        //images
        PersonalData_IMAGE_leftArrow = view.findViewById(R.id.PersonalData_IMAGE_leftArrow);
        PersonalData_IMAGE_backGround = view.findViewById(R.id.PersonalData_IMAGE_backGround);
        //spinner
        PersonalData_Spinner_district = view.findViewById(R.id.PersonalData_Spinner_district);
    }
}
