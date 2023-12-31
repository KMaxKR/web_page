package ks.msx.web_page.controller;

import ks.msx.web_page.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/page")
    public String returnUserPage(Model model){
        return "user_page";
    }
}
