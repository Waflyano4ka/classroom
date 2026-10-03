package ru.classroom.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.classroom.config.jwt.JwtService;
import ru.classroom.dto.LocalLoginUserRequest;
import ru.classroom.dto.LocalLoginUserResponse;
import ru.classroom.entity.User;
import ru.classroom.entity.UserCredentials;
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
    public LocalLoginUserResponse localLogin(LocalLoginUserRequest request) {
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
                String token = jwtService.generateToken(user.getId());
                return new LocalLoginUserResponse(token);
            }
        }

        throw new InvalidCredentialsException("Неверный логин или пароль");
    }
}
