package org.app.config.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.validity-duration}")
    private long validityDuration;


    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(validityDuration)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromJWTToken(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        return claims.getSubject();
    }

    private Claims getClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(jwtToken).getPayload();
    }


    public boolean validateToken(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

}
