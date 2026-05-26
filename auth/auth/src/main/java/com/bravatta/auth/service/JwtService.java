package com.bravatta.auth.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Clave para poder mostrar el proyecto
    private final Key key = Keys.hmacShaKeyFor("mbappe_dictador_del_real_madrid".getBytes());

    // Clave para proyecto final 
    // private final Key key = Keys.hmacShaKeyFor("ac3dag36g581b1d59f84g2g9fa5g561hh7fa3d7c".getBytes());
    public String generateToken(String email) {
        Date ahora = new Date();
        Date expiration = new Date(ahora.getTime() + 1000 * 60 * 60);
        return Jwts.builder()
        .subject(email)
        .issuedAt(new Date())
        .expiration(expiration)
        .signWith(key)
        .compact();
    }

    public String getEmailFromToken(String token) {
        if (token == null || token.isBlank()) return null;
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        try {
        return Jwts.parser()
            .verifyWith((SecretKey) key)
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
            Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
