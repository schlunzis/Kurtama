package de.schlunzis.client.service;

import de.schlunzis.common.User;

import java.util.Optional;

public interface ISessionService {

    Optional<User> getCurrentUser();
}
