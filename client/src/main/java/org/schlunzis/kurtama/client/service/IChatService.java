package org.schlunzis.kurtama.client.service;

import javafx.collections.ObservableList;

public interface IChatService {

    ObservableList<String> getChatMessages();

    String getCurrentUsername();

    void sendMessage(String nickname, String message);

}
