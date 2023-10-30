package de.schlunzis.client.service;

import de.schlunzis.common.IUser;

import java.util.Optional;

public interface ISessionService {

    Optional<IUser> getCurrentUser();
}
