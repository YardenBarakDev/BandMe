package com.bawp.bandme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bawp.bandme.call_back_interface.CallBack_FireBaseEmailChecker;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationLoginInfo;
import com.bawp.bandme.R;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.ValidateUserAccountInfo;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Fragment_LoginInfo extends Fragment {

    protected View view;
    private CallBack_RegistrationLoginInfo callBack_registrationLoginInfo;
    private TextInputLayout LoginInfo_TF_email;
    private TextInputEditText LoginInfo_LBL_email;

    private TextInputLayout LoginInfo_TF_password;
    private TextInputEditText LoginInfo_LBL_password;

    private TextInputLayout LoginInfo_TF_validate_password;
    private TextInputEditText LoginInfo_LBL_validate_password;

    private ImageView LoginInfo_IMAGE_rightArrow;
    private ImageView LoginInfo_IMAGE_backGround;

    //init util ValidateUserAccountInfo to check input
    ValidateUserAccountInfo validateUserAccountInfo;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_login_info, container, false);
        }
        findViews();
        glideBackground();

        //init util to check user input
        validateUserAccountInfo = new ValidateUserAccountInfo();

        //set listener to Image
        LoginInfo_IMAGE_rightArrow.setOnClickListener(onClickListener);

        //set firebase callback
        FireBaseMethods.getInstance().setCallBack_fireBaseEmailChecker(callBack_fireBaseEmailChecker);
        return view;
    }

    private void glideBackground() {
        Glide
                .with(this)
                .load(R.drawable.background_image)
                .into(LoginInfo_IMAGE_backGround);
    }

    public static Fragment_LoginInfo newInstance(){
        return new Fragment_LoginInfo();
    }

    public void setActivityCallBack(CallBack_RegistrationLoginInfo callBack_registrationLoginInfo){
        this.callBack_registrationLoginInfo = callBack_registrationLoginInfo;
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            checkPassword();
            checkMatchedPasswords();

            if (checkEmail()){
                if (LoginInfo_LBL_email.getText() != null)
                FireBaseMethods.getInstance().checkIfEmailExist(LoginInfo_LBL_email.getText().toString());
            }
        }
    };

    private boolean validateInfo() {
        //check the strings are not null
        if (LoginInfo_LBL_email.getText() != null &&
                LoginInfo_LBL_password.getText() != null) {

            boolean validatePassword = checkPassword();
            boolean validateMatchedPasswords = checkMatchedPasswords();

            return validatePassword && validateMatchedPasswords;

        }
        return false;
    }

    private boolean checkMatchedPasswords() {
        //check if null
        if (LoginInfo_LBL_password.getText() == null && LoginInfo_LBL_validate_password.getText() == null)
            return false;

        //check if password and validate password are matched
        if (!validateUserAccountInfo.ValidatePasswordMatch(LoginInfo_LBL_password.getText().toString(),
                LoginInfo_LBL_validate_password.getText().toString() )){
            LoginInfo_TF_validate_password.setError("Password not matched");
            return false;
        }
        else{
            LoginInfo_TF_validate_password.setError("");
            return true;
        }
    }
    private boolean checkPassword(){

        if (LoginInfo_LBL_password.getText() == null)
            return false;

        if (!validateUserAccountInfo.ValidatePasswordRequirements(LoginInfo_LBL_password.getText().toString())){
            LoginInfo_TF_password.setError("Password must have at least 8 characters with at least one Capital letter, at least one lower case letter and at least one number ");
            return false;
        }
        else{
            LoginInfo_TF_password.setError("");
            return true;
        }
    }

     private boolean checkEmail(){
        //check if not null
        if (LoginInfo_LBL_email.getText() == null ){
            return false;
        }

        //check if the user added email
        if (LoginInfo_LBL_email.getText().toString().trim().isEmpty()){
            LoginInfo_TF_email.setError("This field is mandatory");
            return false;
        }
        //check if the email address has a real structure
        else if (!validateUserAccountInfo.validateEmail(LoginInfo_LBL_email.getText().toString())){
            LoginInfo_TF_email.setError("Invalid email format");
            return false;
        }
        else{
            LoginInfo_TF_email.setError("");
        }
        return true;
    }

    //return the answer if the email is already in use from FireBaseMethods
    CallBack_FireBaseEmailChecker callBack_fireBaseEmailChecker = new CallBack_FireBaseEmailChecker() {

        @Override
        public void isEmailExist(boolean exist) {
            if (!exist) {
                LoginInfo_TF_email.setError("");
                nextStep();
            }
            else{
                LoginInfo_TF_email.setError("Email all ready registered");
                Log.d("jjjj", "isEmailExist: false");
            }
        }
    };

    //check if the password and the password validation
    //if they are fine move to the next register page
    private void nextStep(){
        if (validateInfo()){
            if (LoginInfo_LBL_email.getText() != null && LoginInfo_LBL_password.getText() != null && LoginInfo_LBL_validate_password.getText() != null)
            callBack_registrationLoginInfo.advanceLoginInfoStep(
                    LoginInfo_LBL_email.getText().toString().trim(),
                    LoginInfo_LBL_password.getText().toString().trim(),
                    LoginInfo_LBL_validate_password.getText().toString().trim());
        }
    }

    private void findViews() {
        LoginInfo_TF_email = view.findViewById(R.id.LoginInfo_TF_email);
        LoginInfo_LBL_email = view.findViewById(R.id.LoginInfo_LBL_email);

        LoginInfo_TF_password = view.findViewById(R.id.LoginInfo_TF_password);
        LoginInfo_LBL_password = view.findViewById(R.id.LoginInfo_LBL_password);

        LoginInfo_TF_validate_password = view.findViewById(R.id.LoginInfo_TF_validate_password);
        LoginInfo_LBL_validate_password = view.findViewById(R.id.LoginInfo_LBL_validate_password);

        LoginInfo_IMAGE_rightArrow = view.findViewById(R.id.LoginInfo_IMAGE_rightArrow);
        LoginInfo_IMAGE_backGround = view.findViewById(R.id.LoginInfo_IMAGE_backGround);
    }
}