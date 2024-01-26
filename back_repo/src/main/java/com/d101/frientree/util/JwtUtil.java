package com.d101.frientree.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class JwtUtil {

    private static final String key = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> valueMap, int min) {

        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {

            SecretKey key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));

            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJwtException("MalFormed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJwtException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJwtException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJwtException("JWTError");
        } catch (Exception e) {
            throw new CustomJwtException("Error");
        }
        return claim;
    }

    public static Long getExpirationDateFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes(StandardCharsets.UTF_8)); // UTF-8 인코딩 명시
        Claims claims = Jwts.parserBuilder() // parserBuilder() 사용으로 업데이트
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().getTime();
    }

}