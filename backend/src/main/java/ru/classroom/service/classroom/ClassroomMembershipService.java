package ru.classroom.service.classroom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.entity.ClassroomMembership;
import ru.classroom.entity.User;
import ru.classroom.exception.ForbiddenException;
import ru.classroom.exception.NotFoundException;
import ru.classroom.exception.UserNotFoundException;
import ru.classroom.repository.ClassroomMembershipRepository;
import ru.classroom.service.user.UserService;

import java.util.Objects;

@Service
public class ClassroomMembershipService {

    private final ClassroomMembershipRepository classroomMembershipRepository;
    private final UserService userService;

    public ClassroomMembershipService(
            ClassroomMembershipRepository classroomMembershipRepository,
            UserService userService) {
        this.classroomMembershipRepository = classroomMembershipRepository;
        this.userService = userService;
    }

    /**
     * Изменяет состояние избранного у учебного класса.
     * @param classroomMembershipId идентификатор членства в учебном классе
     * @param isFavorite новое состояние избранного
     */
    @Transactional
    public void updateFavorite(String classroomMembershipId, boolean isFavorite) {
        ClassroomMembership membership = getClassroomMembership(classroomMembershipId);
        membership.setFavorite(isFavorite);
    }

    /**
     * Получает членство пользователя в учебном классе и проверяет права доступа.
     * @param classroomMembershipId идентификатор членства в учебном классе
     * @return членство пользователя в учебном классе
     * @throws NotFoundException если членство не найдено
     * @throws ForbiddenException если членство принадлежит другому пользователю,
     * пользователь заблокирован или покинул учебный класс
     * @throws IllegalStateException если авторизованный пользователь не найден
     */
    private ClassroomMembership getClassroomMembership(String classroomMembershipId) {
        ClassroomMembership membership = classroomMembershipRepository.findById(classroomMembershipId)
                .orElseThrow(() -> new NotFoundException(
                        "Данный класс не найден"
                ));

        User currentUser = userService.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException(
                        "Авторизованный пользователь не найден"
                ));

        if (!membership.getUser().equals(currentUser)) {
            throw new ForbiddenException(
                    "У вас нет доступа к этому учебному классу"
            );
        }

        if (membership.isBlocked()) {
            throw new ForbiddenException(
                    "Вы заблокированы в этом учебном классе"
            );
        }

        if (membership.isLeave()) {
            throw new ForbiddenException(
                    "Вы не состоите в данном классе"
            );
        }

        return membership;
    }
}
