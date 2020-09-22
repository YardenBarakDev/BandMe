package com.bawp.bandme;

import java.util.ArrayList;

public class MyBandProfile {

    private String firstName = "";
    private String lastName = "";
    private String password = "";
    private String email = "";
    private String selfInfo = "";
    private String age = "";
    private String district = "";
    private ArrayList<String> instruments;

    public MyBandProfile() {
        this.instruments = new ArrayList<>();
    }

    public MyBandProfile(String firstName, String lastName, String password, String email, String selfInfo, String age, String district, ArrayList<String> instruments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.selfInfo = selfInfo;
        this.age = age;
        this.district = district;
        this.instruments = instruments;
    }

    public String getFirstName() {
        return firstName;
    }

    public MyBandProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public MyBandProfile setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAge() {
        return age;
    }

    public MyBandProfile setAge(String age) {
        this.age = age;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public MyBandProfile setDistrict(String district) {
        this.district = district;
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
