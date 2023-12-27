package ks.msx.web_page.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebPageController {

    @GetMapping("/")
    public String returnMainPage(HttpServletResponse response, Model model){
        model.addAttribute("name", "value");
        response.setStatus(200);
        return "index";
    }
}
