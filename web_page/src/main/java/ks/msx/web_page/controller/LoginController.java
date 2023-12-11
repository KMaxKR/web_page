package ks.msx.web_page.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ks.msx.web_page.entity.UserDTO;
import ks.msx.web_page.repository.UserRepository;
import ks.msx.web_page.utility.JwtUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class LoginController {
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final static String LOGIN_PATH = "/authentication";

    @GetMapping(LOGIN_PATH)
    public String returnLoginPage(){
        return "log_page";
    }

    @PostMapping(LOGIN_PATH + "/login")
    public void logUser(@RequestBody UserDTO userDTO, HttpServletResponse response){
        String token = null;
        Cookie cookie = new Cookie("AUTHORIZATION", token);
        response.addCookie(cookie);
    }
}
