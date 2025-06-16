package com.team_one.expressoh.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Component
public class JWTUtils {

    private SecretKey secretKey;
    public static final long EXPIRATION_TIME = 86400000; // 24 hours

    // Initialize the secret key using an encoded string.
    public JWTUtils() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // Generates a JWT token containing the username and roles as claims.
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles", userDetails.getAuthorities())
                .signWith(secretKey)
                .compact();
    }

    // Generates a refresh token with additional claims.
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // Validates the token by checking the username and expiration.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extracts the username (subject) from the token.
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Generic method to extract a specific claim using a provided resolver function.
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload());
    }

    // Checks if the token is expired.
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}