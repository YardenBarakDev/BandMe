package com.bawp.bandme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bawp.bandme.R;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationPersonalData;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Fragment_PersonalData extends Fragment {

    protected View view;
    private CallBack_RegistrationPersonalData callBack_registrationPersonalData;

    private TextInputLayout PersonalData_TF_firstName;
    private TextInputLayout PersonalData_TF_lastName;
    private TextInputLayout PersonalData_TF_age;
    private TextInputLayout PersonalData_TF_info;
    private MaterialButton PersonalData_BTN_Login;
    private ImageView PersonalData_IMAGE_leftArrow;
    private ImageView PersonalData_IMAGE_backGround;
    private MaterialSpinner PersonalData_Spinner_district;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        }
        findViews();
        glideBackground();
        setListeners();

        return view;
    }

    private void setListeners() {
        PersonalData_IMAGE_leftArrow.setOnClickListener(personalDataListener);
        PersonalData_BTN_Login.setOnClickListener(personalDataListener);
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
        PersonalData_TF_lastName = view.findViewById(R.id.PersonalData_TF_lastName);
        PersonalData_TF_age = view.findViewById(R.id.PersonalData_TF_age);
        PersonalData_TF_info = view.findViewById(R.id.PersonalData_TF_info);
        //button
        PersonalData_BTN_Login = view.findViewById(R.id.PersonalData_BTN_Login);
        //images
        PersonalData_IMAGE_leftArrow = view.findViewById(R.id.PersonalData_IMAGE_leftArrow);
        PersonalData_IMAGE_backGround = view.findViewById(R.id.PersonalData_IMAGE_backGround);
        //spinner
        PersonalData_Spinner_district = view.findViewById(R.id.PersonalData_Spinner_district);
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
                if (validInfo())
                callBack_registrationPersonalData.createAnAccount(Objects.requireNonNull(PersonalData_TF_firstName.getEditText()).getText().toString(),
                        Objects.requireNonNull(PersonalData_TF_lastName.getEditText()).getText().toString(),
                        Objects.requireNonNull(PersonalData_TF_age.getEditText()).getText().toString(),
                        Objects.requireNonNull(PersonalData_TF_info.getEditText()).getText().toString(),
                        PersonalData_Spinner_district.getSelectedItem().toString());
                break;
            case "PersonalData_IMAGE_leftArrow":
                callBack_registrationPersonalData.backToRegisterInstruments();
                break;
        }
    }

    private boolean validInfo() {
        boolean checkFirstName = validFirstName();
        boolean checkLastName = validLastName();
        boolean checkDistrict = validDistrict();

        return checkFirstName && checkLastName && checkDistrict;
    }

    private boolean validFirstName(){
        if (Objects.requireNonNull(PersonalData_TF_firstName.getEditText()).getText().toString().length() < 2){
            PersonalData_TF_firstName.setError("Mandatory field");
            return false;
        }
        else{
            PersonalData_TF_firstName.setError("");
            return true;
        }
    }

    private boolean validLastName() {
        if (Objects.requireNonNull(PersonalData_TF_lastName.getEditText()).getText().toString().length() < 2){
            PersonalData_TF_lastName.setError("Mandatory field");
            return false;
        }
        else{
            PersonalData_TF_lastName.setError("");
            return true;
        }
    }

    private boolean validDistrict() {
        if (PersonalData_Spinner_district.getSelectedItemPosition() == -1){
            PersonalData_Spinner_district.setError("Please choose district");
            return false;
        }
        else{
            PersonalData_Spinner_district.setError("");
            PersonalData_Spinner_district.setErrorColor(PersonalData_Spinner_district.getBaseColor());
            return true;
        }
    }
}