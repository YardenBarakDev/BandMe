package com.bawp.bandme.model;

import java.util.Date;

public class BandMeContact {

    private String chatId = "";
    private String participant = "";
    private String imageURL = "";
    private String firstName = "";
    private String lastName = "";
    private String date = "";
    boolean activeChat = false;

    public BandMeContact() {

    }
    public BandMeContact(String chatId, String participant, String imageURL, String firstName, String lastName) {
        this.chatId = chatId;
        this.participant = participant;
        this.imageURL = imageURL;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public BandMeContact(String chatId, String participant, String imageURL, String firstName, String lastName, String date, boolean activeChat) {
        this.chatId = chatId;
        this.participant = participant;
        this.imageURL = imageURL;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.activeChat = activeChat;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public BandMeContact setDate(String date) {
        this.date = date;
        return this;
    }

    public boolean isActiveChat() {
        return activeChat;
    }

    public BandMeContact setActiveChat(boolean activeChat) {
        this.activeChat = activeChat;
        return this;
    }
}
