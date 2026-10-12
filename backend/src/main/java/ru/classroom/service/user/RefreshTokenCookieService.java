package ru.classroom.service.user;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import ru.classroom.config.jwt.JwtProperties;
import ru.classroom.config.jwt.JwtService;

@Service
public class RefreshTokenCookieService {

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private static final String COOKIE_PATH = "/api/auth";

    private final JwtProperties jwtProperties;
    private final JwtService jwtService;

    public RefreshTokenCookieService(
            JwtProperties jwtProperties,
            JwtService jwtService
    ) {
        this.jwtProperties = jwtProperties;
        this.jwtService = jwtService;
    }

    /**
     * Устанавливает cookie с refresh token
     * @param response HTTP-ответ
     * @param userId id
     */
    public void setRefreshTokenCookie(String userId, HttpServletResponse response) {
        String refreshToken = jwtService.generateRefreshToken(userId);
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(false)
                .path(COOKIE_PATH)
                .maxAge(jwtProperties.refreshExpiration())
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Удаляет refresh-токен из cookie, добавляя в HTTP-ответ заголовок Set-Cookie
     * с пустым значением и нулевым сроком действия.
     * @param response HTTP-ответ, в который добавляется заголовок для удаления cookie
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .path(COOKIE_PATH)
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
