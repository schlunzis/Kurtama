package org.schlunzis.kurtama.client.fx.dialog;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.Objects;

class CreateLobbyDialog extends javafx.scene.control.Dialog<CreateLobbyDialogResult> {

    @FXML
    private TextField lobbyNameField;

    CreateLobbyDialog() {
        initModality(Modality.APPLICATION_MODAL);

        setResultConverter(buttonType -> {
            if (!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                return null;
            }

            return new CreateLobbyDialogResult(lobbyNameField.getText());
        });
    }

    @FXML
    void initialize() {
        setOnShowing(dialogEvent -> lobbyNameField.requestFocus());
    }

}

