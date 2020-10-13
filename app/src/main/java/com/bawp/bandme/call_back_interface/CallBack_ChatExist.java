package com.bawp.bandme.call_back_interface;

public interface CallBack_ChatExist {

    void getCurrentUserData();
    void noPreviousConversations();
    void hasPreviousConversations(String key);
}
