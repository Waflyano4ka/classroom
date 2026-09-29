package ru.classroom.service.user;

import ru.classroom.dto.LocalLoginUserRequest;
import ru.classroom.entity.User;
import ru.classroom.repository.UserRepository;

public class UserAuthenticationService extends UserService {

    public UserAuthenticationService(
            UserRepository userRepository
    ) {
        super(
                userRepository
        );
    }

//    public void login(LocalLoginUserRequest request) {
//        User user = findUserByLoginOrEmail(request.getLogin());
//
//        if (user == null || !passwordEncoder.matches(
//                request.getPassword(),
//                userCredentialsRepository.findById(user.getId())
//                        .map(UserCredentials::getPasswordHash)
//                        .orElse(null)
//        )) {
//            throw new IllegalArgumentException("Неверный логин или пароль");
//        }
//    }
}
