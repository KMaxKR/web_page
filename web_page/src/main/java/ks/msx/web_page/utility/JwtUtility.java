package ks.msx.web_page.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ks.msx.web_page.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JwtUtility {
    private final static String key = "javaLangTestJwt20RE21Specs=JRUWTaskKREFGH19";
    public String generateToken(UserEntity user){
        String token = "";
        try {
            token = Jwts.builder()
                    .setSubject(user.getUsername())
                    .claim("role", user.getRole())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 60*60*1000000L))
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact();
        }catch (Exception e){
            e.getStackTrace();
        }
        return "Bearer " + token;
    }

    private Claims getClaim(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token){
        return getClaim(token).getSubject();
    }

    public boolean isValidToken(String token){
        return getClaim(token).getExpiration().before(new Date());
    }
}
