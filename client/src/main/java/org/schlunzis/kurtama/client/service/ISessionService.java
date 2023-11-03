package org.schlunzis.kurtama.client.service;

import org.schlunzis.kurtama.common.IUser;

import java.util.Optional;

public interface ISessionService {

    Optional<IUser> getCurrentUser();
}
