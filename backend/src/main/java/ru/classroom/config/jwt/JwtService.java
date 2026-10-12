package ru.classroom.config.jwt;

import io.jsonwebtoken.Claims;
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

    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

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
     * Создаёт access token для указанного пользователя
     * @param userId идентификатор пользователя
     * @return подписанный access token
     */
    public String generateAccessToken(String userId) {
        return generateToken(userId, ACCESS_TOKEN_TYPE, jwtProperties.expiration());
    }

    /**
     * Создаёт refresh token для указанного пользователя
     * @param userId идентификатор пользователя
     * @return подписанный refresh token
     */
    public String generateRefreshToken(String userId) {
        return generateToken(userId, REFRESH_TOKEN_TYPE, jwtProperties.refreshExpiration());
    }

    /**
     * Создаёт подписанный JWT с идентификатором пользователя, типом токена
     * и сроком действия.
     *
     * @param userId идентификатор пользователя, для которого создаётся токен
     * @param tokenType тип токена, например access или refresh
     * @param expiration срок действия токена
     * @return подписанный JWT в виде строки
     */
    private String generateToken(
            String userId,
            String tokenType,
            java.time.Duration expiration
    ) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expiration);

        return Jwts.builder()
                .subject(userId)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Проверяет access token и извлекает идентификатор пользователя.
     * @param token JWT-токен
     * @return идентификатор пользователя или {@code null}, если токен недействителен
     */
    @Nullable
    public String getUserIdFromAccessToken(String token) {
        return getUserId(token, ACCESS_TOKEN_TYPE);
    }

    /**
     * Проверяет refresh token и извлекает идентификатор пользователя.
     *
     * @param token JWT-токен
     * @return идентификатор пользователя или {@code null}, если токен недействителен
     */
    @Nullable
    public String getUserIdFromRefreshToken(String token) {
        return getUserId(token, REFRESH_TOKEN_TYPE);
    }

    /**
     * Извлекает идентификатор пользователя из JWT после проверки подписи,
     * срока действия и типа токена.
     *
     * @param token JWT, из которого необходимо получить идентификатор пользователя
     * @param expectedType ожидаемый тип токена, например access или refresh
     * @return идентификатор пользователя, если токен действителен и его тип совпадает
     * с ожидаемым; {@code null} в противном случае
     */
    @Nullable
    private String getUserId(String token, String expectedType) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (!expectedType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
                return null;
            }

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}