package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.RegisterUserRequest;
import ru.classroom.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрирует нового пользователя с использованием локальной аутентификации.
     * @param request данные для регистрации пользователя
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void localRegister(@Valid @RequestBody RegisterUserRequest request) {
        userService.localRegister(request);
    }
}
