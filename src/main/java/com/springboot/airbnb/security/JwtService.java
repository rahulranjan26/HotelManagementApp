package com.springboot.airbnb.security;

import com.springboot.airbnb.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret_key}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUserId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRoles().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 1000))
                .signWith(getSecretKey())
                .compact();

    }


    public String getRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getUserId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 365 * 24))
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());

    }

}
