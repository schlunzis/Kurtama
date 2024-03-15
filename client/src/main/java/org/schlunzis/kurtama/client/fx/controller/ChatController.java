package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane root;
    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField chatTextField;
    @FXML
    private TextField senderNameTextField;
    @FXML
    private Button scrollDownButton;

    private volatile boolean paused = false;
    private ScrollBar verticalBar;

    @FXML
    public void initialize() {
        chatListView.setItems(chatService.getChatMessages());
        senderNameTextField.setText(chatService.getCurrentUsername());
        chatListView.setCellFactory(listView -> new CustomListCell());
        chatService.getChatMessages().addListener((ListChangeListener<String>) change -> onNewChatMessage());
        root.widthProperty().addListener((observable, oldValue, newValue) -> updateScrollDownButtonPosition());
        scrollDownButton.widthProperty().addListener((observable, oldValue, newValue) -> updateScrollDownButtonPosition());
        chatTextField.heightProperty().addListener((observable, oldValue, newValue) -> updateScrollDownButtonPosition());
        setPaused(false);
    }

    @FXML
    private void sendMessage() {
        String text = chatTextField.getText().strip();
        String senderName = senderNameTextField.getText().strip();
        if (text.isBlank())
            return;
        chatService.sendMessage(senderName, text);
        chatTextField.setText("");
    }

    @FXML
    private void onScrollDownButton() {
        scrollDown();
        setPaused(false);
    }

    private void onNewChatMessage() {
        if (verticalBar == null) {
            verticalBar = (ScrollBar) chatListView.lookup(".scroll-bar:vertical");
            verticalBar.valueProperty().addListener((obs, oldValue, newValue) ->
                    setPaused(newValue.doubleValue() < verticalBar.getMax())
            );
        }
        if (paused) return;
        Platform.runLater(this::scrollDown);
    }

    private void scrollDown() {
        chatListView.scrollTo(chatService.getChatMessages().size() - 1);
    }

    private void updateScrollDownButtonPosition() {
        double newButtonPositionLeft = (root.getWidth() / 2.0) - (scrollDownButton.getWidth() / 2.0);
        double newButtonPositionTop = chatTextField.getHeight() + 10.0;
        AnchorPane.setLeftAnchor(scrollDownButton, newButtonPositionLeft);
        AnchorPane.setBottomAnchor(scrollDownButton, newButtonPositionTop);
    }

    private void setPaused(boolean paused) {
        scrollDownButton.setVisible(paused);
        this.paused = paused;
    }

    private static class CustomListCell extends ListCell<String> {
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
    }

}
