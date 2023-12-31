package ks.msx.web_page.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class test {

    @GetMapping("/test")
    public String returnPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
