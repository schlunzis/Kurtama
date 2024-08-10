package org.schlunzis.kurtama.server.rest.user;

import org.schlunzis.kurtama.common.IUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest/user/v1")
public class UserController {

    @GetMapping("view")
    public ResponseEntity<IUser> view() {
        // TODO: implement
        return ResponseEntity.ok(null);
    }
}