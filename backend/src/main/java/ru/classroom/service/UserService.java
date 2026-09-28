package ru.classroom.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.dto.RegisterUserRequest;
import ru.classroom.entity.AccountStatus;
import ru.classroom.entity.AuthProvider;
import ru.classroom.entity.User;
import ru.classroom.entity.UserCredentials;
import ru.classroom.repository.AccountStatusRepository;
import ru.classroom.repository.AuthProviderRepository;
import ru.classroom.repository.UserCredentialsRepository;
import ru.classroom.repository.UserRepository;
import ru.classroom.util.IdGenerator;

@Service
public class UserService {

    private static final String USER_ID_PREFIX = "user-";
    private static final String ACTIVE_UNVERIFIED_STATUS = "ACTIVE_UNVERIFIED";
    private static final String LOCAL_AUTH_PROVIDER = "LOCAL";

    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final AccountStatusRepository accountStatusRepository;
    private final AuthProviderRepository authProviderRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    public UserService(
            UserRepository userRepository,
            UserCredentialsRepository userCredentialsRepository,
            AccountStatusRepository accountStatusRepository,
            AuthProviderRepository authProviderRepository,
            PasswordEncoder passwordEncoder,
            IdGenerator idGenerator
    ) {
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.accountStatusRepository = accountStatusRepository;
        this.authProviderRepository = authProviderRepository;
        this.passwordEncoder = passwordEncoder;
        this.idGenerator = idGenerator;
    }

    /**
     * Регистрирует пользователя с использованием локальной аутентификации.
     * При регистрации создаются пользователь и его учётные данные.
     * @param request данные для регистрации пользователя
     */
    @Transactional
    public void localRegister(RegisterUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username уже занят");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email уже занят");
        }

        AccountStatus accountStatus = accountStatusRepository
                .findByCode(ACTIVE_UNVERIFIED_STATUS)
                .orElseThrow(() -> new IllegalStateException(
                        "Статус ACTIVE_UNVERIFIED не найден"
                ));

        AuthProvider authProvider = authProviderRepository
                .findByCode(LOCAL_AUTH_PROVIDER)
                .orElseThrow(() -> new IllegalStateException(
                        "Провайдер LOCAL не найден"
                ));

        String userId = generateUserId();

        User user = new User(
                userId,
                request.getUsername(),
                request.getEmail(),
                accountStatus,
                authProvider
        );

        UserCredentials credentials = new UserCredentials(
                userId,
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
        userCredentialsRepository.save(credentials);
    }

    /**
     * Генерирует уникальный идентификатор пользователя.
     * @return уникальный идентификатор пользователя
     */
    private String generateUserId() {
        String userId;

        do {
            userId = USER_ID_PREFIX + idGenerator.generate();
        } while (userRepository.existsById(userId));

        return userId;
    }
}
