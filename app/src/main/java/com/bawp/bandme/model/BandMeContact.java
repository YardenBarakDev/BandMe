package com.bawp.bandme.model;

public class BandMeContact {

    private String chatId = "";
    private String participant = "";
    private String imageURL = "";
    private String firstName = "";
    private String lastName = "";
    private String lastUpdate = "";
    private String lastMessage = "";
    private boolean active = false;

    public BandMeContact() {
    }

    public BandMeContact(String chatId, String participant, String imageURL, String firstName, String lastName) {
        this.chatId = chatId;
        this.participant = participant;
        this.imageURL = imageURL;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public BandMeContact setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public BandMeContact setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public BandMeContact setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }
}
