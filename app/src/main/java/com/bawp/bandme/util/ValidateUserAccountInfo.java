package com.bawp.bandme.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUserAccountInfo {

    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public ValidateUserAccountInfo() {
    }

    //check if the password length is between 8-20 and contain upper letter, lowercase letter and number
    public boolean ValidatePasswordRequirements(String password){
        if (password.length() > 7 && password.length() < 21){
            char ch;
            boolean capitalFlag = false;
            boolean lowerCaseFlag = false;
            boolean numberFlag = false;
            for(int i=0;i < password.length();i++) {
                ch = password.charAt(i);
                if( Character.isDigit(ch)) {
                    numberFlag = true;
                }
                else if (Character.isUpperCase(ch)) {
                    capitalFlag = true;
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true;
                }
                if(numberFlag && capitalFlag && lowerCaseFlag)
                    return true;
            }
        }
        return false;
    }

    //check if both if the password match to the validation password section
    public boolean ValidatePasswordMatch(String password, String passwordValidation){
        return password.equals(passwordValidation);
    }

    public boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public boolean validateNames(String name){
        char ch;
        for (int i = 0; i < name.length(); i++) {
            ch = name.charAt(i);
            if (ch < 65 || ch >122 || ch > 90 && ch <97)
                return false;
        }
        return true;
    }
}
