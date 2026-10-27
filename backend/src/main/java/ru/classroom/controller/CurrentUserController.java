package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.user.CurrentUserResponse;
import ru.classroom.dto.user.UpdateCurrentUserRequest;
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

    /**
     * Обновляет данные текущего авторизованного пользователя.
     * Проверяет уникальность логина и электронной почты при их изменении.
     * @param request данные для обновления профиля пользователя
     * @return обновлённые данные пользователя
     */
    @PutMapping("/me")
    public CurrentUserResponse updateCurrentUser(@Valid @RequestBody UpdateCurrentUserRequest request) {
        return userService.updateCurrentUser(request);
    }
}
