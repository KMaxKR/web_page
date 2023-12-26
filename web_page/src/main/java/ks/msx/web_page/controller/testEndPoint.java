package ks.msx.web_page.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class testEndPoint {
    @GetMapping("/test")
    public String returnPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/tes")
    public String getPage(Principal principal){
        return principal.getName();
    }
}
