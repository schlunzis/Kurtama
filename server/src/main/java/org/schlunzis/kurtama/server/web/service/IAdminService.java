package org.schlunzis.kurtama.server.web.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IAdminService {

    default ResponseEntity<Void> deleteUser(UUID id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
