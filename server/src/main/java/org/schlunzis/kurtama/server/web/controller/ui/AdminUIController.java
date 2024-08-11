package org.schlunzis.kurtama.server.web.controller.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUIController {

    private final IUserStore userStore;

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("usermanagement")
    public String usermanagement(Model model) {
        model.addAttribute("users", userStore.getAllUsers());
        return "admin/usermanagement";
    }

}
