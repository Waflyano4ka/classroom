package ru.classroom.service.user;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.dto.CurrentUserResponse;
import ru.classroom.dto.UpdateCurrentUserRequest;
import ru.classroom.entity.User;
import ru.classroom.exception.ConflictException;
import ru.classroom.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    protected final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Находит пользователя по логину или адресу электронной почты.
     * @param login логин или адрес электронной почты пользователя
     * @return найденный пользователь или {@code null}, если пользователь не найден
     */
    @Nullable
    protected User findUserByLoginOrEmail(String login) {
        return userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .orElse(null);
    }


    /**
     * Возвращает DTO с информацией о текущем авторизованном пользователе.
     * @return информация о текущем пользователе
     * @throws AuthenticationCredentialsNotFoundException если не удалось определить текущего пользователя
     * @throws UsernameNotFoundException если учётная запись пользователя не найдена
     */
    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUserResponse() {
        return getCurrentUser().map(this::toCurrentUserResponse)
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось найти вашу учётную запись"));
    }

    /**
     * Обновляет данные авторизованного пользователя.
     * Проверяет уникальность логина и электронной почты,
     * сохраняет изменения и возвращает обновлённые данные пользователя.
     * @param request данные для обновления пользователя
     * @return обновлённые данные авторизованного пользователя
     * @throws IllegalStateException если авторизованный пользователь не найден
     * @throws ConflictException если логин или электронная почта уже заняты
     */
    @Transactional
    public CurrentUserResponse updateCurrentUser(UpdateCurrentUserRequest request) {
        User user = getCurrentUser()
                .orElseThrow(() -> new IllegalStateException(
                        "Авторизованный пользователь не найден"
                ));

        if (!user.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new ConflictException("Логин уже занят");
            }
            user.setUsername(request.getUsername());
        }

        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Эта почта уже зарегистрирована");
            }
            user.setEmail(request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMiddleName(request.getMiddleName());

        return toCurrentUserResponse(user);
    }

    /**
     * Возвращает текущего авторизованного пользователя.
     * @return текущий пользователь или пустой {@link Optional}, если учётная запись не найдена
     * @throws AuthenticationCredentialsNotFoundException если не удалось определить текущего пользователя
     */
    protected Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof String userId)) {
            throw new AuthenticationCredentialsNotFoundException("Не удалось определить текущего пользователя");
        }

        return userRepository.findById(userId);
    }

    /**
     * Преобразует сущность пользователя в DTO для передачи клиенту.
     * @param user сущность пользователя
     * @return DTO с данными пользователя
     */
    private CurrentUserResponse toCurrentUserResponse(User user) {
        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getLastName(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getAccountStatus().getName(),
                user.getCreatedAt()
        );
    }
}
