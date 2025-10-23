package life_ecom_logic_core.eddie.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private final Key signingKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("Property 'jwt.secret' is missing.");
        }

        byte[] keyBytes = decodeSecret(secret.trim());
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("jwt.secret must be at least 256 bits (32 bytes) after decoding/interpretation.");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] decodeSecret(String secret) {
        try {
            return java.util.Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            log.info("jwt.secret is not valid Base64, using as plain text key");
            return secret.getBytes(StandardCharsets.UTF_8);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Object v = extractClaims(token).get("role");
        return v != null ? v.toString() : null;
    }

    public String extractUserId(String token) {
        Object v = extractClaims(token).get("userId");
        return v != null ? v.toString() : null;
    }

    public String extractName(String token) {
        Object v = extractClaims(token).get("name");
        return v != null ? v.toString() : null;
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}