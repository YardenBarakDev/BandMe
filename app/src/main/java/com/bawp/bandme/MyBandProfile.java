package com.bawp.bandme;

import java.util.ArrayList;

public class MyBandProfile {

    private String userName = "";
    private String password = "";
    private String email = "";
    private String selfInfo = "";
    private ArrayList<String> instruments;

    public MyBandProfile() {
        this.instruments = new ArrayList<>();
    }

    public MyBandProfile(String userName, String password, String email, String selfInfo, ArrayList<String> instruments) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.selfInfo = selfInfo;
        this.instruments = instruments;
    }

    public String getUserName() {
        return userName;
    }

    public MyBandProfile setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MyBandProfile setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MyBandProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSelfInfo() {
        return selfInfo;
    }

    public MyBandProfile setSelfInfo(String selfInfo) {
        this.selfInfo = selfInfo;
        return this;
    }

    public ArrayList<String> getInstruments() {
        return instruments;
    }

    public MyBandProfile setInstruments(ArrayList<String> instruments) {
        this.instruments = instruments;
        return this;
    }
}
