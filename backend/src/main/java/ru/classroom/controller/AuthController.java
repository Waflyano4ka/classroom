package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.LocalRegisterUserRequest;
import ru.classroom.service.user.UserRegistrationService;
import ru.classroom.service.user.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    public AuthController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
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
}
