package de.schlunzis.server.user;

import java.util.Optional;
import java.util.UUID;

public interface IUserStore {

    UUID createUser(ServerUser user);

    Optional<ServerUser> getUser(UUID uuid);

    Optional<ServerUser> getUser(String email);


}
