package org.schlunzis.kurtama.server.web.controller.ui;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ErrorUIController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        int status = Integer.parseInt(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());

        log.debug("Handling error status: {}", status);

        if (status == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        } else if (status == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error/500";
        }
        return "error/default";
    }

}
