package ru.classroom.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.classroom.dto.CurrentUserResponse;
import ru.classroom.service.user.UserService;

@RestController
@RequestMapping("/api/users")
public class CurrentUserController {

    private final UserService userService;

    public CurrentUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Возвращает информацию о текущем авторизованном пользователе.
     * @return информация о текущем пользователе
     */
    @GetMapping("/me")
    public CurrentUserResponse getCurrentUser() {
        return userService.getCurrentUserResponse();
    }
}
