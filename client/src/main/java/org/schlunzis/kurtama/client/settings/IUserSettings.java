package org.schlunzis.kurtama.client.settings;

import org.schlunzis.kurtama.client.server.ServerType;

public interface IUserSettings {

    void putString(Setting<String> key, String value);

    void putInt(Setting<Integer> key, int value);

    void putBoolean(Setting<Boolean> key, Boolean value);

    String getString(Setting<String> key);

    int getInt(Setting<Integer> key);

    boolean getBoolean(Setting<Boolean> key);

    ServerType getServerType(Setting<ServerType> key);

    void putServerType(Setting<ServerType> key, ServerType value);

}
