package org.schlunzis.kurtama.server.web.controller.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.web.api.UserApi;
import org.schlunzis.kurtama.server.web.service.IUserService;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Getter
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final IUserService service;

}
