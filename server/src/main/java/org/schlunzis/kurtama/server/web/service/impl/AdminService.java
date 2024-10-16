package org.schlunzis.kurtama.server.web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.schlunzis.kurtama.server.web.service.IAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    private final IUserStore userStore;

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        if (userStore.deleteUser(id)) {
            log.debug("deleted user with id {}", id);
            return ResponseEntity.ok().build();
        } else {
            log.debug("user with id {} not found and thus not deleted", id);
            return ResponseEntity.notFound().build();
        }
    }
}
