package org.schlunzis.kurtama.client.fx.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DialogFactory {

    private Stage stage;

    @EventListener
    public void onClientReadyEvent(ClientReadyEvent event) {
        this.stage = event.stage();
    }

    public Optional<Dialog<CreateLobbyDialogResult>> createCreateLobbyDialog() {
        CreateLobbyDialog dialog = new CreateLobbyDialog();

        try {
            final FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/schlunzis/kurtama/client/fx/controller/createLobby.fxml")
            );
            loader.setController(dialog);
            final DialogPane pane = loader.load();
            dialog.setDialogPane(pane);
            dialog.initOwner(stage);
        } catch (IOException e) {
            log.error("Could not load dialog!", e);
            return Optional.empty();
        }

        return Optional.of(dialog);
    }

}
