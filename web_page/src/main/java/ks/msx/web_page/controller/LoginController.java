package ks.msx.web_page.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class LoginController {
    private final static String LOGIN_PATH = "/authentication";

    @GetMapping(LOGIN_PATH)
    public String returnLoginPage(){
        return "log_page";
    }
}
