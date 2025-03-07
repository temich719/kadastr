package com.example.kadastr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
public class JwtProvider {

    private static final String BEARER_TOKEN = "Bearer";
    private static final String AUTHORIZATION = "Authorization";

    @Value("${token.privateKey}")
    private String privateKey;

    public String generateToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) //number of millis in 1 hour
                .signWith(getPrivateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Optional<String> extractToken(HttpServletRequest request) {
        Optional<String> token = Optional.empty();
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (nonNull(bearerToken) && bearerToken.startsWith(BEARER_TOKEN)) {
            token = Optional.of(bearerToken.substring(BEARER_TOKEN.length() + 1));
        }
        return token;
    }

    public String getLoginFromToken(String token) {
        return extractClaim(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        return getLoginFromToken(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return !extractClaim(token).getExpiration().before(new Date());
    }

    private Claims extractClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPrivateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getPrivateKey() {
        return Keys.hmacShaKeyFor(privateKey.getBytes());
    }

}
