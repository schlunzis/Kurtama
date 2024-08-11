package org.schlunzis.kurtama.server.web.controller.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.web.api.AdminApi;
import org.schlunzis.kurtama.server.web.service.IAdminService;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Getter
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final IAdminService service;

}
