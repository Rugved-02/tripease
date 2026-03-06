package com.tripease.auth.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tripease.auth.service.UserService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; 

    public String generateToken(String email, String userId) {
    	log.info("Generating JWT token for user : email: {}", email);


        log.info("Generating JWT token for user: userId: {}", userId);

        

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", email);       // Matches: claims.get("userId", String.class)
        extraClaims.put("userId", userId);

                return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(getSignInKey())
                .compact();
    }

    public String extractEmail(String token) {
        try {
            String email =  Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            log.debug("Extracted email from token: {}", email);
            return email;
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);

            log.debug("Token validation successful");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token validation failed: Token has expired");
        } catch (SignatureException e) {
            log.warn("Token validation failed: Invalid signature");
        } catch (MalformedJwtException e) {
            log.warn("Token validation failed: Malformed token");
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}