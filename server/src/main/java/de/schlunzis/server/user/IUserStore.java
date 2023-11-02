package de.schlunzis.server.user;

import java.util.Optional;
import java.util.UUID;

public interface IUserStore {

    UUID createUser(DBUser user);

    Optional<DBUser> getUser(UUID uuid);

    Optional<DBUser> getUser(String email);

}
