package ks.msx.web_page.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ks.msx.web_page.entity.UserDTO;
import ks.msx.web_page.repository.UserRepository;
import ks.msx.web_page.service.UserService;
import ks.msx.web_page.utility.JwtUtility;
import lombok.AllArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class LoginController {
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    // PATH: http://localhost:8090/login/oauth2/code/google


    public final static String PATH = "/authentication";
    public final static String OAUTH_PATH = "/oauth2/authorize-client";
    Map<String, String> oauth2RegistrationUrls = new HashMap<>();

    @GetMapping(PATH)
    public String returnLoginPage(HttpServletResponse response) {
        response.setStatus(200);
        return "log_page";
    }


    @PostMapping(PATH + "/login")
    public void logUser(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        HttpServletResponse response,
                        HttpServletRequest request) throws IOException{
        String token = jwtUtility.generateToken(userRepository.findUserByUsername(username).orElseThrow());
        authenticate(username, password);

//        Cookie cookie = new Cookie("AUTHORIZATION", URLEncoder.encode(token, StandardCharsets.UTF_8));
//        cookie.setMaxAge(10000000);
//        response.addCookie(cookie);

        HttpSession session = request.getSession();
        session.setAttribute("AUTHORIZATION", token);
        response.sendRedirect("/");
    }

    @GetMapping(PATH + "/reg")
    public String returnRegistrationPage(HttpServletResponse response) {
        response.setStatus(200);
        return "reg_page";
    }



    @PostMapping(PATH + "/registration")
    public void registerUser(@RequestParam(name = "username") String username,
                             @RequestParam(name = "password") String password,
                             HttpServletResponse response) throws IOException {
        userService.registerUser(UserDTO.builder()
                .username(username)
                .password(password)
                .build());
        authenticate(username, password);
        response.setStatus(200);
        response.sendRedirect("/");
    }

    @GetMapping("/log/oauth2")
    public String returnLoginPageOauth2(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        clientRegistrations.forEach(registration -> oauth2RegistrationUrls.put(registration.getClientName(), OAUTH_PATH + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2RegistrationUrls);
        return "log_oauth";
    }

    @GetMapping("/success/log")
    public void getUserInfo(OAuth2AuthenticationToken user, HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }


    @GetMapping(PATH+"/logout")
    public void logoutUser(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        if (request.isRequestedSessionIdValid() && session != null) {
            session.invalidate();
        }
        response.sendRedirect("/");
    }

    private void authenticate(String username, String password) {
        try {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
