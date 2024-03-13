package org.schlunzis.kurtama.client.settings;

public interface IUserSettings {

    void putString(Setting<String> key, String value);

    void putInt(Setting<Integer> key, int value);

    void putBoolean(Setting<Boolean> key, Boolean value);

    String getString(Setting<String> key);

    int getInt(Setting<Integer> key);

    boolean getBoolean(Setting<Boolean> key);

}
