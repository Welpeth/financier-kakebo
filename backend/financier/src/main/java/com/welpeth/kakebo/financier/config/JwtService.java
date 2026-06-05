package com.welpeth.kakebo.financier.config;

import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final String SECRET = "REMOVED-JWT-SECRET";

  public String generateToken(Holder holder) {
    return Jwts.builder()
        .subject(holder.getEmail())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 86400000))
        .signWith(
            Keys.hmacShaKeyFor(SECRET.getBytes()),
            Jwts.SIG.HS256
        )
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
          .build()
          .parseSignedClaims(token);

      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
