package org.schlunzis.kurtama.client.fx.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.NotificationPane;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEventType;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeMessage;
import org.schlunzis.kurtama.client.util.I18n;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractController implements MessageShowingController {

    private static final List<String> NOTIFICATION_STYLES;

    static {
        NOTIFICATION_STYLES = Arrays.stream(SceneChangeEventType.values())
                .map(e -> "notification-pane" + e.name().toLowerCase())
                .toList();
    }

    protected final I18n i18n;
    private final Deque<SceneChangeMessage> queue = new ArrayDeque<>();

    protected abstract NotificationPane getNotificationPane();

    protected void initNotificationPane() {
        getNotificationPane().setOnHidden(e -> showNextMessage());
    }

    @Override
    public void showMessages(List<SceneChangeMessage> messages) {
        queue.addAll(messages);
        if (!getNotificationPane().isShowing()) {
            showNextMessage();
        }
    }

    private void showNextMessage() {
        SceneChangeMessage message = queue.poll();
        if (message ==  null)
            return;
        log.info("Showing message {}", message);

        NotificationPane notificationPane = getNotificationPane();
        notificationPane.textProperty().bind(i18n.createBinding(message.getMessageKey()));
        notificationPane.getStyleClass().removeAll(NOTIFICATION_STYLES);
        notificationPane.getStyleClass().add("notification-pane-" + message.getType().name().toLowerCase());
        notificationPane.show();
    }

}
