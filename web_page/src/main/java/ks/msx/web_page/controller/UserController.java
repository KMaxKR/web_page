package ks.msx.web_page.controller;

import ks.msx.web_page.entity.UserEntity;
import ks.msx.web_page.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/page")
    public String returnUserPage(Model model, Principal principal){
        UserEntity user = (UserEntity) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user_page";
    }
}
