package ks.msx.web_page.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class LoginController {
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
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
                        HttpServletResponse response) throws IOException{
        String token = jwtUtility.generateToken(userRepository.findUserByUsername(username).orElseThrow());
        authenticate(username, password);

        Cookie cookie = new Cookie("AUTHORIZATION", URLEncoder.encode(token, StandardCharsets.UTF_8));
        cookie.setMaxAge(10000000);
        response.addCookie(cookie);
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
    public String getUserInfo(OAuth2AuthenticationToken authentication, HttpServletResponse response) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            Cookie cookie = new Cookie("AUTHORIZATION", URLEncoder.encode(client.getAccessToken().getTokenValue(), StandardCharsets.UTF_8));
            response.addCookie(cookie);
        }
        return "index";
    }


    @GetMapping(PATH+"/logout")
    public void logoutUser(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        if (request.isRequestedSessionIdValid() && session != null) {
            session.invalidate();
        }
        clearCookie(request, response);
        Cookie cookie = new Cookie("AUTHORIZATION", null);
        response.addCookie(cookie);
        response.sendRedirect("/");
    }

    private void clearCookie(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies){
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    private void authenticate(String username, String password) {
        try {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
