package org.schlunzis.kurtama.server.web.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The IUserService interface provides methods to interact with the account of a user. It is used by the user itself via the
 * REST API.
 */
public interface IUserService {

    /**
     * Provides a summary of the user's account.
     *
     * @return the summary of the user's account
     */
    default ResponseEntity<Void> summary() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
