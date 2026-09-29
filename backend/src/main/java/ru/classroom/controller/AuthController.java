package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.LocalLoginUserRequest;
import ru.classroom.dto.LocalLoginUserResponse;
import ru.classroom.dto.LocalRegisterUserRequest;
import ru.classroom.service.user.UserAuthenticationService;
import ru.classroom.service.user.UserRegistrationService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;

    public AuthController(
            UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService
    ) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
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
    public LocalLoginUserResponse localLogin(@Valid @RequestBody LocalLoginUserRequest request) {
        return userAuthenticationService.localLogin(request);
    }
}
