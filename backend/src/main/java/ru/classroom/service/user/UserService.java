package ru.classroom.service.user;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.dto.CurrentUserResponse;
import ru.classroom.entity.User;
import ru.classroom.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

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
        return getCurrentUser().map(user -> new CurrentUserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getLastName(),
                        user.getFirstName(),
                        user.getMiddleName(),
                        user.getAccountStatus().getName(),
                        user.getCreatedAt()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось найти вашу учётную запись"));
    }

    /**
     * Возвращает текущего авторизованного пользователя.
     * @return текущий пользователь или пустой {@link Optional}, если учётная запись не найдена
     * @throws AuthenticationCredentialsNotFoundException если не удалось определить текущего пользователя
     */
    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof String userId)) {
            throw new AuthenticationCredentialsNotFoundException("Не удалось определить текущего пользователя");
        }

        return userRepository.findById(userId);
    }
}
