package ks.msx.web_page.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ks.msx.web_page.entity.UserDTO;
import ks.msx.web_page.entity.UserEntity;
import ks.msx.web_page.repository.UserRepository;
import ks.msx.web_page.utility.JwtUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
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



    @PostMapping(path=LOGIN_PATH + "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public void logUser(@RequestParam(name = "username")String username,
                        @RequestParam(name = "password")String password,
                        HttpServletResponse response) throws IOException {
        authenticate(username, password);
        String token = jwtUtility.generateToken(userRepository.findUserByUsername(username).orElseThrow());
        Cookie cookie = new Cookie("AUTHORIZATION", URLEncoder.encode(token, StandardCharsets.UTF_8));
        response.addCookie(cookie);
        response.sendRedirect("/");
    }



    private void authenticate(String username, String password){
        try {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, password));
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
