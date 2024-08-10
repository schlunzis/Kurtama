package org.schlunzis.kurtama.server.user;

import org.schlunzis.kurtama.common.IUser;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface IUserStore {

    UUID createUser(DBUser user);

    Optional<DBUser> getUser(UUID uuid);

    Optional<DBUser> getUser(String email);

    boolean deleteUser(IUser User);

    boolean deleteUser(UUID id);

    List<IUser> getAllUsers();

}
