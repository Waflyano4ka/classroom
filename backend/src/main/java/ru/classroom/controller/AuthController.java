package ru.classroom.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.auth.*;
import ru.classroom.service.user.RefreshTokenCookieService;
import ru.classroom.service.user.UserAuthenticationService;
import ru.classroom.service.user.UserRegistrationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    public AuthController(
            UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService,
            RefreshTokenCookieService refreshTokenCookieService
    ) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.refreshTokenCookieService = refreshTokenCookieService;
    }

    /**
     * Регистрирует нового пользователя с использованием локальной аутентификации.
     * @param request данные для регистрации пользователя
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void localRegister(@Valid @RequestBody LocalRegisterUserRequest request) {
        userRegistrationService.localRegister(request);
    }

    /**
     * Выполняет локальную аутентификацию пользователя.
     * @param request данные для аутентификации пользователя
     * @return ответ с JWT-токеном
     */
    @PostMapping("/login")
    public LocalLoginUserResponse localLogin(@Valid @RequestBody LocalLoginUserRequest request, HttpServletResponse response) {
        LocalLoginResult result = userAuthenticationService.localLogin(request, response);
        refreshTokenCookieService.setRefreshTokenCookie(result.getUserId(), response);

        return result.getLocalLoginUserResponse();
    }

    /**
     * Завершает пользовательскую сессию, удаляя refresh-токен из cookie.
     * @param response HTTP-ответ, в который записывается удаление cookie
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        refreshTokenCookieService.clearRefreshTokenCookie(response);
    }

    /**
     * Обновляет access-токен на основании refresh-токена из cookie.
     *
     * @param refreshToken refresh-токен из cookie; может отсутствовать
     * @param response HTTP-ответ, используемый для удаления недействительной cookie
     * @return новый access-токен при успешном обновлении или сообщение об ошибке,
     * если refresh-токен отсутствует, недействителен или истёк
     */
    @PostMapping("/refresh")
    public ResponseEntity<LocalLoginUserResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        String accessToken = userAuthenticationService.refreshAccessToken(refreshToken);

        if (accessToken == null) {
            refreshTokenCookieService.clearRefreshTokenCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //todo Что-то придумать для ответа пользователю
        }

        return ResponseEntity.ok(new LocalLoginUserResponse(accessToken));
    }

    /**
     * Изменяет пароль текущего авторизованного пользователя.
     * @param request данные для изменения пароля
     */
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userAuthenticationService.changePassword(request);
    }
}
