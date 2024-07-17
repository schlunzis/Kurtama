package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.schlunzis.zis.fx.bindings.BindingsFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tmp {

    private static Map<String, Field> fieldMap = null;

    public static void i18n(BindingsFactory bf, LoginController c) {
        findFXMLAnnotatedFields(c);
        try {
            ((Label) fieldMap.get("emailLabel").get(c)).textProperty().bind(bf.createBinding("login.label.email"));
            ((Label) fieldMap.get("passwordLabel").get(c)).textProperty().bind(bf.createBinding("login.label.password"));
            ((Button) fieldMap.get("registerButton").get(c)).textProperty().bind(bf.createBinding("login.button.register"));
            ((Button) fieldMap.get("loginButton").get(c)).textProperty().bind(bf.createBinding("login.button.login"));
            ((Label) fieldMap.get("serverLabel").get(c)).textProperty().bind(bf.createBinding("login.label.server"));
            ((Label) fieldMap.get("portLabel").get(c)).textProperty().bind(bf.createBinding("login.label.port"));
            ((Button) fieldMap.get("connectButton").get(c)).textProperty().bind(bf.createBinding("login.button.connect"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void findFXMLAnnotatedFields(LoginController c) {
        if (fieldMap == null) {
            List<Field> controllerFields = Arrays.stream(c.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(FXML.class))
                    .toList();
            controllerFields.forEach(f -> f.setAccessible(true));
            fieldMap = new HashMap<>();
            controllerFields.forEach(f -> fieldMap.put(f.getName(), f));
        }
    }

}