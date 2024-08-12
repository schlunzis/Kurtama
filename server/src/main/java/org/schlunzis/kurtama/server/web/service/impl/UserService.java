package org.schlunzis.kurtama.server.web.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.web.service.IUserService;
import org.schlunzis.kurtama.server.web.service.WebUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class UserService implements IUserService {

    @Override
    public ResponseEntity<Void> summary() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebUser webUser = (WebUser) auth.getPrincipal();
        UUID userId = webUser.getId();
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
