package ks.msx.web_page.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ks.msx.web_page.service.UserService;
import ks.msx.web_page.utility.JwtUtility;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtility jwtUtility;
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tk = request.getHeader("AUTH");
        String requestToken = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (Objects.equals(cookie.getName(), "AUTHORIZATION")) {
//                    response.addCookie(new Cookie("AUTHORIZATION", cookie.getValue()));
//                    requestToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
//                }
//            }
//        }

        try{
            requestToken = request.getSession().getAttribute("AUTHORIZATION").toString();
        }catch (Exception e){
            if (tk != null){
                requestToken = tk;
            }
            e.getStackTrace();
        }

        String username = null;
        String token = null;

        if (requestToken != null && requestToken.startsWith("Bearer ")){
            token = requestToken.substring(7);
            try {
                username = jwtUtility.getUsernameFromToken(token);
            }catch (Exception e){
                System.out.println("Token expirated");
                e.getStackTrace();
            }
        }else {
            System.out.println("Warn Token does not start with Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (!jwtUtility.isValidToken(token)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
                System.out.println(usernamePasswordAuthenticationToken.isAuthenticated());
            }
        }
        filterChain.doFilter(request, response);
    }
}
