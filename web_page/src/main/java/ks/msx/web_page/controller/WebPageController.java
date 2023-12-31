package ks.msx.web_page.controller;

import jakarta.servlet.http.HttpServletResponse;
import ks.msx.web_page.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class WebPageController {
    private final ProductService productService;

    @GetMapping("/")
    public String returnMainPage(HttpServletResponse response, Model model){
        model.addAttribute("product", productService.getAllProducts());
        response.setStatus(200);
        return "index";
    }
}
