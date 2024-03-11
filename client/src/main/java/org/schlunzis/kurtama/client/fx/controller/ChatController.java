package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.service.IChatService;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("chat.fxml")
@Component
@RequiredArgsConstructor
public class ChatController {

    private final IChatService chatService;

    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField chatTextField;
    @FXML
    private TextField senderNameTextField;

    private volatile boolean paused = false;

    @FXML
    public void initialize() {
        chatListView.setItems(chatService.getChatMessages());
        senderNameTextField.setText(chatService.getCurrentUsername());
        chatListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                } else {
                    setText(message);
                }
                this.setFocusTraversable(false);
                this.setMouseTransparent(true);
                this.setDisable(true);
            }
        });
        chatListView.setOnScrollStarted(event -> {
            paused = true;
            log.debug("Paused scrolling");
        });
        chatListView.setOnScrollFinished(event -> {
            paused = false;
            log.debug("Resumed scrolling");
        });
        chatService.getChatMessages().addListener((ListChangeListener<String>) change -> {
            Platform.runLater(this::onNewChatMessage);
            onNewChatMessage();
            log.debug("Chat messages changed");
        });
    }

    @FXML
    private void sendMessage() {
        String text = chatTextField.getText();
        if (text.isBlank())
            return;
        chatService.sendMessage(senderNameTextField.getText(), text);
        chatTextField.setText("");
    }

    private void onNewChatMessage() {
        if (paused) return;
        log.debug("Scrolling to bottom");
        chatListView.scrollTo(chatService.getChatMessages().size() - 1);
    }

}
