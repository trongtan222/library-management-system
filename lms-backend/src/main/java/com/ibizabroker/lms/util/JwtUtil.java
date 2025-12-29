// src/main/java/com/ibizabroker/lms/util/JwtUtil.java
package com.ibizabroker.lms.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.io.DecodingException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

  @Value("${app.jwt-secret:dev-secret-change-me-32-bytes-minimum!!}")
  private String secret; // >= 32 bytes (hoặc base64)

  @Value("${app.jwt-expiration:86400000}")
  private long expirationMs;

  private SecretKey key() {
    try {
        // Vẫn giữ nguyên: Thử giải mã Base64 trước
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    } catch (DecodingException e) { 
        // SỬA LẠI: Bắt đúng DecodingException
        // Dùng secret như văn bản thuần túy (plain text) với mã hóa UTF-8
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

private Date currentTime = null;

public void setCurrentTime(Date currentTime) {
    this.currentTime = currentTime;
}

private Date now() {
    return currentTime != null ? currentTime : new Date();
}

// ⭐ BẮT ĐẦU THÊM TỪ ĐÂY
  /**
   * Dùng cho mục đích Test (Unit Test)
   */
  public void setSecret(String secret) {
      this.secret = secret;
  }

  /**
   * Dùng cho mục đích Test (Unit Test)
   */
  public void setExpirationMs(long expirationMs) {
      this.expirationMs = expirationMs;
  }
  // ⭐ KẾT THÚC THÊM Ở ĐÂY

  /** ✅ Dùng hàm này: thêm extra claims (userId, roles, ...) */
  public String generateToken(UserDetails user, Map<String, Object> extra) {
    Date now = now();
    Date exp = new Date(now.getTime() + expirationMs);
    Map<String, Object> claims = (extra == null) ? new HashMap<>() : new HashMap<>(extra);
    return Jwts.builder()
        .claims(claims)
        .subject(user.getUsername())
        .issuedAt(now)
        .expiration(exp)
        .signWith(key())
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return getClaim(token, Claims::getSubject);
  }

  public boolean validateToken(String token, UserDetails user) {
    try {
      String username = getUsernameFromToken(token);
      return username != null && username.equals(user.getUsername()) && !isExpired(token);
    } catch (ExpiredJwtException e) {
      return false;
    }
  }

  private boolean isExpired(String token) {
    Date exp = getClaim(token, Claims::getExpiration);
    return exp.before(now());
  }

  private <T> T getClaim(String token, Function<Claims, T> resolver) {
    return resolver.apply(parseAllClaims(token));
  }

  private Claims parseAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(key())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /** ❌ Không dùng nữa */
  @Deprecated
  public String generateToken(String username) {
    throw new UnsupportedOperationException("Use generateToken(UserDetails, extraClaims)");
  }
}
