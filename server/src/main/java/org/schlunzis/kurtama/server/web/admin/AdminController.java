package org.schlunzis.kurtama.server.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping
    public String index() {
        return "admin/index";
    }

    @RequestMapping("usermanagement")
    public String usermanagement() {
        return "admin/usermanagement";
    }

}
