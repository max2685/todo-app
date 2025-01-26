package org.app.config.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JWTService {

    private static final String SECRET_KEY = "220A1EC50F1FCF5D547C8A78E0F459120780BB374FC9E1B90FCFCE3F1D14D6381733878836A5414C0A0B1250363144BE9B928F54304BD0D0CEEBE0E966E79779";
    private static final long VALIDITY_OF_TOKEN = TimeUnit.MINUTES.toMillis(30);

    AeadAlgorithm enc = Jwts.ENC.A256GCM;
    SecretKey key = enc.key().build();

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY_OF_TOKEN)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromJWTToken(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        return claims.getSubject();
    }

    private Claims getClaims(String jwtToken) {
//        return Jwts.parser().decryptWith(key).build().parseEncryptedClaims(jwtToken).getPayload();
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(jwtToken).getPayload();
    }


    public boolean validateToken(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

}
