package com.bawp.bandme.model;

import java.util.ArrayList;

public class BandMeProfile {

    private String firstName = "";
    private String lastName = "";
    private String selfInfo = "";
    private String age = "";
    private String district = "";
    private ArrayList<String> instruments;

    public BandMeProfile() {
        this.instruments = new ArrayList<>();
    }

    public BandMeProfile(String firstName, String lastName, String selfInfo, String age, String district, ArrayList<String> instruments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.selfInfo = selfInfo;
        this.age = age;
        this.district = district;
        this.instruments = instruments;
    }

    public String getFirstName() {
        return firstName;
    }

    public BandMeProfile setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public BandMeProfile setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getSelfInfo() {
        return selfInfo;
    }

    public BandMeProfile setSelfInfo(String selfInfo) {
        this.selfInfo = selfInfo;
        return this;
    }

    public String getAge() {
        return age;
    }

    public BandMeProfile setAge(String age) {
        this.age = age;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public BandMeProfile setDistrict(String district) {
        this.district = district;
        return this;
    }

    public ArrayList<String> getInstruments() {
        return instruments;
    }

    public BandMeProfile setInstruments(ArrayList<String> instruments) {
        this.instruments = instruments;
        return this;
    }
}
