package org.schlunzis.kurtama.common;

import java.util.Collection;
import java.util.UUID;

public interface ILobby {

    UUID getId();

    String getName();

    Collection<IUser> getUsers();

}
