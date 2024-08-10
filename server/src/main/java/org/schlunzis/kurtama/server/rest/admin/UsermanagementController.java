package org.schlunzis.kurtama.server.rest.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/rest/admin/v1/usermanagement")
@RequiredArgsConstructor
public class UsermanagementController {

    private final IUserStore userStore;

    @GetMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        log.debug("delete user with id {}", id);
        userStore.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
