package org.schlunzis.kurtama.client.util;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class I18nBinder {

    public static final String BINDING_PREFIX = "i{";
    public static final String BINDING_SUFFIX = "}";

    private final I18n i18n;

    public void createBindings(Parent parent) {
        Collection<Node> children = parent.getChildrenUnmodifiable();
        for (Node child : children) {
            handleNode(child);
        }
    }

    private void handleNode(Node node) {
        switch (node) {
            case Label label -> handleLabel(label);
            case Button button -> handleButton(button);
            case Parent parent -> createBindings(parent);
            default -> throw new IllegalStateException("Unexpected value: " + node);
        }
    }

    private void handleLabel(Label label) {
        if (isI18nBindingKey(label.getText())) {
            label.textProperty().bind(i18n.createBinding(extractKey(label.getText())));
        }
    }

    private void handleButton(Button button) {
        if (isI18nBindingKey(button.getText())) {
            button.textProperty().bind(i18n.createBinding(extractKey(button.getText())));
        }
    }

    private static boolean isI18nBindingKey(String text) {
        return text.startsWith(BINDING_PREFIX) && text.endsWith(BINDING_SUFFIX);
    }

    private static String extractKey(String text) {
        return text.substring(BINDING_PREFIX.length(), text.length() - BINDING_SUFFIX.length());
    }

}
