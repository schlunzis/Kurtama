package org.schlunzis.kurtama.client.settings;

import org.springframework.stereotype.Component;

import java.util.prefs.Preferences;

@Component
public class UserSettings implements IUserSettings {

    private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    @Override
    public void putString(Setting<String> key, String value) {
        preferences.put(key.getPreferencesKey(), value);
    }

    @Override
    public void putInt(Setting<Integer> key, int value) {
        preferences.putInt(key.getPreferencesKey(), value);
    }

    @Override
    public void putBoolean(Setting<Boolean> key, Boolean value) {
        preferences.putBoolean(key.getPreferencesKey(), value);
    }

    @Override
    public String getString(Setting<String> key) {
        return preferences.get(key.getPreferencesKey(), key.getDefaultValue());
    }

    @Override
    public int getInt(Setting<Integer> key) {
        return preferences.getInt(key.getPreferencesKey(), key.getDefaultValue());
    }

    @Override
    public boolean getBoolean(Setting<Boolean> key) {
        return preferences.getBoolean(key.getPreferencesKey(), key.getDefaultValue());
    }

}
