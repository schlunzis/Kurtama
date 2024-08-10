package org.schlunzis.kurtama.server.web.admin;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

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
