package com.welpeth.kakebo.financier.config;

import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey secretKey;
  private final long expirationMs;

  public JwtService(
      @Value("${application.security.jwt.secret}") String secret,
      @Value("${application.security.jwt.expiration:86400000}") long expirationMs) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(Holder holder) {
    return Jwts.builder()
        .subject(holder.getEmail())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
