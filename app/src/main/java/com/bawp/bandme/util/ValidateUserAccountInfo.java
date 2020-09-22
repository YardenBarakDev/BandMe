package com.bawp.bandme.util;

public class ValidateUserAccountInfo {

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
}
