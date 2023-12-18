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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@AllArgsConstructor
public class LoginController {
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    public final static String LOGIN_PATH = "/authentication";



    @GetMapping(LOGIN_PATH)
    public String returnLoginPage(HttpServletResponse response){
        response.setStatus(200);
        return "log_page";
    }



    @PostMapping(LOGIN_PATH + "/login")
    public void logUser(@RequestBody UserDTO userDTO, HttpServletResponse response) throws IOException {
        String token = jwtUtility.generateToken(userRepository.findUserByUsername(userDTO.getUsername()).orElseThrow());
        Cookie cookie = new Cookie("AUTHORIZATION", URLEncoder.encode(token, StandardCharsets.UTF_8));
        response.addCookie(cookie);
        response.sendRedirect("/");
    }
}
