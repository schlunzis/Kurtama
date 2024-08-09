package org.schlunzis.kurtama.client.settings;

import lombok.Getter;

import java.io.File;
import java.util.Locale;

@Getter
public class Setting<T> {

    public static final Setting<String> HOST = new Setting<>("host", "127.0.0.1");
    public static final Setting<Integer> PORT = new Setting<>("port", 8007);
    public static final Setting<String> LANGUAGE = new Setting<>("language", Locale.US.toLanguageTag());
    public static final Setting<String> SERVER_PATH = new Setting<>("server-path", System.getProperty("user.home") + File.separator + ".kurtama");

    private final String preferencesKey;
    private final T defaultValue;

    private Setting(String preferencesKey, T defaultValue) {
        this.preferencesKey = preferencesKey;
        this.defaultValue = defaultValue;
    }

}
