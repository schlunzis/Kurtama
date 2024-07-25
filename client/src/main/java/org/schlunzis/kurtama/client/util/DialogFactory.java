package org.schlunzis.kurtama.client.util;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DialogFactory {

    private final I18n i18n;

    /**
     * Creates a dialog for password input. The dialog has a title, a header and a content.
     * <p>
     * Obtain and use the dialog as follows:
     * <pre>{@code
     * Dialog<String> dialog = dialogFactory.createPasswordDialog("title.key", "header.key", "content.key");
     * dialog.showAndWait().ifPresent(password -> {
     *      // Do something with the password
     * });
     *  } </pre>
     * <p>
     * The text will be loaded for the current language. If the dialog is opened and the language is changed, the text will not be updated.
     *
     * @param titleKey   The key for the title of the dialog
     * @param headerKey  The key for the header of the dialog
     * @param contentKey The key for the content of the dialog
     * @return The dialog
     * @see <a href="https://stackoverflow.com/a/53825771">Stackoverflow</a>
     */
    public Dialog<String> createPasswordDialog(String titleKey, String headerKey, String contentKey) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(i18n.i18n(titleKey));
        dialog.setHeaderText(i18n.i18n(headerKey));
        Label img = new Label();
        img.getStyleClass().addAll("alert", "confirmation", "dialog-pane");
        dialog.setGraphic(img);// Custom graphic
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField pwd = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label(i18n.i18n(contentKey)), pwd);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });
        Platform.runLater(pwd::requestFocus);
        return dialog;
    }

    /**
     * Creates a dialog for text input. The dialog has a title, a header and a content. Compared to
     * {@link #createPasswordDialog} the dialog has a text field instead of a password field.
     *
     * @param titleKey   The key for the title of the dialog
     * @param headerKey  The key for the header of the dialog
     * @param contentKey The key for the content of the dialog
     * @return The dialog
     */
    public TextInputDialog createTextInputDialog(String titleKey, String headerKey, String contentKey) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(i18n.i18n(titleKey));
        dialog.setHeaderText(i18n.i18n(headerKey));
        dialog.setContentText(i18n.i18n(contentKey));
        return dialog;
    }

}
