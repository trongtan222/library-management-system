// src/main/java/com/ibizabroker/lms/util/JwtUtil.java
package com.ibizabroker.lms.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

  @Value("${app.jwt-secret}")
  private String secret; // >= 32 bytes (hoặc base64)

  @Value("${app.jwt-expiration:86400000}")
  private long expirationMs;

  private SecretKey key() {
    try {
      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    } catch (IllegalArgumentException e) {
      return Keys.hmacShaKeyFor(secret.getBytes());
    }
  }

  /** ✅ Dùng hàm này: thêm extra claims (userId, roles, ...) */
  public String generateToken(UserDetails user, Map<String, Object> extra) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);
    Map<String, Object> claims = (extra == null) ? new HashMap<>() : new HashMap<>(extra);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setIssuedAt(now)
        .setExpiration(exp)
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return getClaim(token, Claims::getSubject);
  }

  public boolean validateToken(String token, UserDetails user) {
    String username = getUsernameFromToken(token);
    return username != null && username.equals(user.getUsername()) && !isExpired(token);
  }

  private boolean isExpired(String token) {
    Date exp = getClaim(token, Claims::getExpiration);
    return exp.before(new Date());
  }

  private <T> T getClaim(String token, Function<Claims, T> resolver) {
    return resolver.apply(parseAllClaims(token));
  }

  private Claims parseAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /** ❌ Không dùng nữa */
  @Deprecated
  public String generateToken(String username) {
    throw new UnsupportedOperationException("Use generateToken(UserDetails, extraClaims)");
  }
}
