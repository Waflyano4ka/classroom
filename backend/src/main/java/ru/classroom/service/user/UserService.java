package ru.classroom.service.user;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.classroom.entity.User;
import ru.classroom.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
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
}
