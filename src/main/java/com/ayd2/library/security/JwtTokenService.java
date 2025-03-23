package com.ayd2.library.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.ayd2.library.config.JwtProperties;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(String username) {
        return generateToken(username, jwtProperties.getAccess().getSecretKey(),
                jwtProperties.getAccess().getExpirationTime());
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, jwtProperties.getRefresh().getSecretKey(),
                jwtProperties.getRefresh().getExpirationTime());
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtProperties.getAccess().getSecretKey());
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtProperties.getRefresh().getSecretKey());
    }

    public String getUsernameFromAccessToken(String token) {
        return getUsernameFromToken(token, jwtProperties.getAccess().getSecretKey());
    }

    public String getUsernameFromRefreshToken(String token) {
        return getUsernameFromToken(token, jwtProperties.getRefresh().getSecretKey());
    }

    public String getUsernameFromToken(String token, String secretKey) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String secretKey) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private String generateToken(String username, String secretKey, long expirationTime) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }
}