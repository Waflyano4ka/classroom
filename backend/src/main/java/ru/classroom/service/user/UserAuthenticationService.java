package ru.classroom.service.user;

import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.config.jwt.JwtService;
import ru.classroom.dto.auth.ChangePasswordRequest;
import ru.classroom.dto.auth.LocalLoginResult;
import ru.classroom.dto.auth.LocalLoginUserRequest;
import ru.classroom.dto.auth.LocalLoginUserResponse;
import ru.classroom.entity.User;
import ru.classroom.entity.UserCredentials;
import ru.classroom.exception.ConflictException;
import ru.classroom.exception.InvalidCredentialsException;
import ru.classroom.repository.UserCredentialsRepository;
import ru.classroom.repository.UserRepository;

@Service
public class UserAuthenticationService extends UserService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserAuthenticationService(
            UserRepository userRepository,
            UserCredentialsRepository userCredentialsRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        super(userRepository);
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Выполняет локальную аутентификацию пользователя по логину и паролю.
     * При успешной аутентификации создаёт JWT-токен.
     *
     * @param request данные для аутентификации пользователя
     * @return ответ с JWT-токеном
     * @throws IllegalArgumentException если пользователь не найден или указан неверный пароль
     * @throws IllegalStateException если для пользователя отсутствуют данные для аутентификации
     */
    public LocalLoginResult localLogin(LocalLoginUserRequest request, HttpServletResponse response) {
        User user = findUserByLoginOrEmail(request.getLogin());

        if (user != null) {
            UserCredentials credentials = userCredentialsRepository
                    .findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "У пользователя отсутствуют данные для аутентификации"
                    ));

            if (passwordEncoder.matches(
                    request.getPassword(),
                    credentials.getPasswordHash()
            )) {
                String accessToken = jwtService.generateAccessToken(user.getId());
                return new LocalLoginResult(
                        user.getId(),
                        new LocalLoginUserResponse(accessToken)
                );
            }
        }

        throw new InvalidCredentialsException("Неверный логин или пароль");
    }

    /**
     * Обновляет access-токен на основании refresh-токена.
     * Проверяет наличие и действительность refresh-токена, а также существование
     * пользователя, после чего генерирует новый access-токен.
     * @param refreshToken refresh-токен пользователя
     * @return новый access-токен, если refresh-токен действителен и пользователь существует;
     * {@code null} в противном случае
     */
    @Nullable
    public String refreshAccessToken(String refreshToken) {
        if (refreshToken == null) {
            return null;
        }

        // todo: Проверять refresh-токен по записи в БД и поддержать отзыв токенов.
        String userId = jwtService.getUserIdFromRefreshToken(refreshToken);
        if (userId == null || !userRepository.existsById(userId)) {
            return null;
        }

        return jwtService.generateAccessToken(userId);
    }

    /**
     * Изменяет пароль текущего авторизованного пользователя.
     * Проверяет правильность текущего пароля и убеждается, что новый пароль
     * отличается от текущего.
     * @param request данные для изменения пароля
     * @throws IllegalStateException если пользователь не найден или отсутствуют
     *                                данные для аутентификации
     * @throws IllegalArgumentException если текущий пароль указан неверно
     *                                   или новый пароль совпадает с текущим
     */
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser()
                .orElseThrow(() -> new IllegalStateException(
                        "Авторизованный пользователь не найден"
                ));

        UserCredentials credentials = userCredentialsRepository
                .findById(user.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "У пользователя отсутствуют данные для аутентификации"
                ));

        if (!passwordEncoder.matches(request.getCurrentPassword(), credentials.getPasswordHash())) {
            throw new InvalidCredentialsException("Неверный текущий пароль");
        }

        if (passwordEncoder.matches(request.getNewPassword(), credentials.getPasswordHash())) {
            throw new ConflictException("Новый пароль должен отличаться от текущего");
        }

        credentials.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    }
}
