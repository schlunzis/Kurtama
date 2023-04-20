package de.schlunzis.client.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.common.messages.chat.ClientChatMessage;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ChatController {

    private final EventBus eventBus;
    private final List<String> messagesToAppend;
    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField chatTextField;
    @FXML
    private TextField senderNameTextField;

    ChatController(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
        messagesToAppend = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        chatListView.getItems().addAll(messagesToAppend);
        messagesToAppend.clear();
    }

    @FXML
    private void sendMessage() {
        String text = chatTextField.getText();
        if (text.isEmpty())
            return;
        eventBus.post(new ClientChatMessage(UUID.randomUUID(), senderNameTextField.getText(), text));
        chatTextField.setText("");
    }

    private void appendMessage(String sender, String message) {
        Platform.runLater(() -> {
            if (chatListView == null)
                messagesToAppend.add(sender + ": " + message);
            else
                chatListView.getItems().add(sender + ": " + message);
        });
    }

    @Subscribe
    void onServerChatMessage(ServerChatMessage message) {
        log.debug("Received message from server: {}", message);
        appendMessage(message.getSender(), message.getMessage());
    }
}
