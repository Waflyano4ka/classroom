package ru.classroom.config.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = new SecretKeySpec(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
    }

    /**
     * Создаёт JWT-токен для указанного пользователя.
     * @param userId идентификатор пользователя
     * @return подписанный JWT-токен
     */
    public String generateToken(String userId) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtProperties.expiration());

        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Проверяет JWT-токен и извлекает из него идентификатор пользователя.
     * @param token JWT-токен
     * @return идентификатор пользователя или {@code null}, если токен недействителен
     */
    @Nullable
    public String getUserId(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
