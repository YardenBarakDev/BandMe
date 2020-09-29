package com.bawp.bandme.model;

public class ChatID {

    private String chatId = "";
    private String participant = "";


    public ChatID() {
    }

    public ChatID(String chatId, String participant) {
        this.chatId = chatId;
        this.participant = participant;
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

    @Override
    public String toString() {
        return "ChatID{" +
                "chatId='" + chatId + '\'' +
                ", participant='" + participant + '\'' +
                '}';
    }
}
