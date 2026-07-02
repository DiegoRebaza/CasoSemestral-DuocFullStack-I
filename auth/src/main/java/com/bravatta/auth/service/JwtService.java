package com.bravatta.auth.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        Date ahora = new Date();
        Date expiration = new Date(ahora.getTime() + 1000 * 60 * 60); // 1 hora

        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(expiration)
                .signWith(getKey(), Jwts.SIG.HS512)
                .compact();
    }

    public String getEmailFromToken(String token) {
        if (token == null || token.isBlank()) return null;
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isValid(String token) {
        if (token == null || token.isBlank()) return false;
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    
}