package com.apexify.logic.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JSON Web Token (JWT) operations.
 * Manages token generation, validation, and extraction of claims.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for JWT signing and verification
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Token expiration time in milliseconds (1 day)
     */
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Generates a JWT token for a given email
     *
     * @param email The email to be included in the token
     * @return Generated JWT token string
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * Extracts the username (email) from a JWT token
     *
     * @param token The JWT token
     * @return The email extracted from the token
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Validates a JWT token against an email
     *
     * @param token The JWT token to validate
     * @param email The email to validate against
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token, String email) {
        return (email.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    /**
     * Extracts claims from a JWT token
     *
     * @param token The JWT token
     * @return Claims object containing token payload
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if a JWT token has expired
     *
     * @param token The JWT token to check
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Retrieves the secret key used for JWT operations
     *
     * @return The secret key string
     */
    public String getSecretKey() {
        return SECRET_KEY;
    }
}
