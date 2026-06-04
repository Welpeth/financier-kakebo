package com.welpeth.kakebo.financier.config;

import com.welpeth.kakebo.financier.domain.user.entity.Holder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final String SECRET = "yabZOM63cN76QFQnKADMAAE2NyuH4cFi0LpI0O6j7tv";

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

}
