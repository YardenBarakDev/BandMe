package com.bawp.bandme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bawp.bandme.call_back_interface.CallBack_RegistrationLoginInfo;
import com.bawp.bandme.R;
import com.bawp.bandme.util.FireBaseMethods;
import com.bawp.bandme.util.ValidateUserAccountInfo;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class Fragment_LoginInfo extends Fragment {

    protected View view;
    private CallBack_RegistrationLoginInfo callBack_registrationLoginInfo;

    private TextInputLayout LoginInfo_TF_email;
    private TextInputLayout LoginInfo_TF_password;
    private TextInputLayout LoginInfo_TF_validate_password;
    private ImageView LoginInfo_IMAGE_rightArrow;
    private ImageView LoginInfo_IMAGE_backGround;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (view == null){
            view = inflater.inflate(R.layout.fragment_login_info, container, false);
        }
        findViews();
        glideBackground();
        LoginInfo_IMAGE_rightArrow.setOnClickListener(onClickListener);
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

    private void findViews() {
        LoginInfo_TF_email = view.findViewById(R.id.LoginInfo_TF_email);
        LoginInfo_TF_password = view.findViewById(R.id.LoginInfo_TF_password);
        LoginInfo_TF_validate_password = view.findViewById(R.id.LoginInfo_TF_validate_password);
        LoginInfo_IMAGE_rightArrow = view.findViewById(R.id.LoginInfo_IMAGE_rightArrow);
        LoginInfo_IMAGE_backGround = view.findViewById(R.id.LoginInfo_IMAGE_backGround);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (validateInfo()){
                callBack_registrationLoginInfo.advanceLoginInfoStep(
                        LoginInfo_TF_email.getEditText().getText().toString().trim(),
                        LoginInfo_TF_password.getEditText().getText().toString().trim(),
                        LoginInfo_TF_validate_password.getEditText().getText().toString().trim());
            }
        }
    };

    private boolean validateInfo() {
        //check the strings are not null
        if (LoginInfo_TF_email.getEditText().getText() != null &&
                LoginInfo_TF_password.getEditText().getText() != null &&
                LoginInfo_TF_validate_password.getEditText().getText() != null) {

            boolean validateEmail = checkEmail();
            boolean validatePassword = checkPassword();
            boolean validateMatchedPasswords = checkMatchedPasswords();

            return validateEmail && validatePassword && validateMatchedPasswords;

        }
        return false;
    }

    private boolean checkMatchedPasswords() {
        ValidateUserAccountInfo validateUserAccountInfo = new ValidateUserAccountInfo();
        if (!validateUserAccountInfo.ValidatePasswordMatch(Objects.requireNonNull(LoginInfo_TF_password.getEditText()).getText().toString(),
                Objects.requireNonNull(LoginInfo_TF_validate_password.getEditText()).getText().toString() )){
            LoginInfo_TF_validate_password.setError("Password not matched");
            return false;
        }
        else{
            LoginInfo_TF_validate_password.setError("");
            return true;
        }
    }

    public boolean checkPassword(){
        ValidateUserAccountInfo validateUserAccountInfo = new ValidateUserAccountInfo();
        if (!validateUserAccountInfo.ValidatePasswordRequirements(Objects.requireNonNull(LoginInfo_TF_password.getEditText()).getText().toString())){
            LoginInfo_TF_password.setError("Password must have at least 8 characters with at least one Capital letter, at least one lower case letter and at least one number ");
            return false;
        }
        else{
            LoginInfo_TF_password.setError("");
            return true;
        }

    }
     public boolean checkEmail(){
        ValidateUserAccountInfo validateUserAccountInfo = new ValidateUserAccountInfo();
        //check if the user added email
        if (LoginInfo_TF_email.getEditText().getText().toString().trim().isEmpty()){
            LoginInfo_TF_email.setError("This field is mandatory");
            return false;
        }
        //check if the email address has a real structure
        else if (!validateUserAccountInfo.validateEmail(LoginInfo_TF_email.getEditText().getText().toString())){
            LoginInfo_TF_email.setError("Invalid email");
            return false;
        }
        else if(FireBaseMethods.getInstance().checkIfEmailExist(LoginInfo_TF_email.getEditText().getText().toString())){
            LoginInfo_TF_email.setError("Email all ready registered");
        }
        else{
            LoginInfo_TF_email.setError("");
        }
        return true;
    }
}