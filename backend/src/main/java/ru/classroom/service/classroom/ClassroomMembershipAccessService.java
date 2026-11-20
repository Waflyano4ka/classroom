package ru.classroom.service.classroom;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.classroom.entity.ClassroomMembership;
import ru.classroom.entity.User;
import ru.classroom.exception.ForbiddenException;
import ru.classroom.exception.NotFoundException;
import ru.classroom.exception.UserNotFoundException;
import ru.classroom.repository.ClassroomMembershipRepository;
import ru.classroom.service.user.UserService;

@Service
public class ClassroomMembershipAccessService {

    private final ClassroomMembershipRepository classroomMembershipRepository;
    private final UserService userService;

    ClassroomMembershipAccessService(
            ClassroomMembershipRepository classroomMembershipRepository,
            UserService userService
    ) {
        this.classroomMembershipRepository = classroomMembershipRepository;
        this.userService = userService;
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
    public ClassroomMembership getClassroomMembership(String classroomMembershipId) {
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

    public ClassroomMembership getClassroomMembershipRole(String classroomMembershipId, String roleCode) {
        ClassroomMembership membership = getClassroomMembership(classroomMembershipId);
        if (!roleCode.equals(membership.getRole().getCode())) {
            throw new AccessDeniedException("Недостаточно прав для выполнения операции");
        }

        return membership;
    }
}
