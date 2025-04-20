package com.event.login_service.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/*
 * This class is used to generate the JWT token and validate the JWT token
 * It uses the io.jsonwebtoken library to generate and validate the JWT token
 * It uses the @Value annotation to get the secret key and expiration time from the application.properties file
 * It uses the jwt secret and jwt expiration time to generate the JWT token
 * It uses the jwt secret to sign the JWT token
 * It uses the jwt expiration time to set the expiration time of the JWT token
 *  
 */

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationMs;

    //generation of the key and token


    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email, jwtExpirationMs);
    }

    
    private String createToken(Map<String, Object> claims, String subject, int expiration) {

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}