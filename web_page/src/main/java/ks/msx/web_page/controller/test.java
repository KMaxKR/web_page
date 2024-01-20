package ks.msx.web_page.controller;

import ks.msx.web_page.utility.JwtUtility;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class test {
    private final JwtUtility jwtUtility;

    @GetMapping("/test")
    public String returnPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @GetMapping("/test/google")
    public String testSuccessAuthWithGoogleService(@AuthenticationPrincipal OAuth2User principal){
        return principal.getName();
    }

    @GetMapping("/test/t")
    public String getTimeToken(Principal principal){
        return "t";
    }
}
