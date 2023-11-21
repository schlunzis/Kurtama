package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.messages.chat.ClientChatMessage;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@FxmlView("chat.fxml")
@Component
@RequiredArgsConstructor
public class ChatController {

    private final ApplicationEventPublisher eventBus;
    private final ISessionService sessionService;
    private final List<String> messagesToAppend = new ArrayList<>();

    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField chatTextField;
    @FXML
    private TextField senderNameTextField;

    @FXML
    public void initialize() {
        chatListView.getItems().addAll(messagesToAppend);
        messagesToAppend.clear();
        String name = sessionService.getCurrentUser().map(IUser::getUsername).orElse("Jonas Doe");
        senderNameTextField.setText(name);


    }

    @FXML
    private void sendMessage() {
        String text = chatTextField.getText();
        if (text.isEmpty())
            return;
        eventBus.publishEvent(new ClientChatMessage(UUID.randomUUID(), senderNameTextField.getText(), text));
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

    @EventListener
    public void onServerChatMessage(ServerChatMessage message) {
        log.debug("Received message from server: {}", message);
        appendMessage(message.getNickname(), message.getMessage());
    }
}
