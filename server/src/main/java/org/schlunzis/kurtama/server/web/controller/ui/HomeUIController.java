package org.schlunzis.kurtama.server.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeUIController {

    @GetMapping
    public String index() {
        return "redirect:home";
    }

    @GetMapping("home")
    public String home() {
        return "index";
    }

}
