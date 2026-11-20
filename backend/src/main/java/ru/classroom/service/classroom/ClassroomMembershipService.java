package ru.classroom.service.classroom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.dto.classroommembership.NameClassroomResponse;
import ru.classroom.entity.Classroom;
import ru.classroom.entity.ClassroomMembership;
import ru.classroom.entity.Role;
import ru.classroom.entity.User;
import ru.classroom.exception.ConflictException;
import ru.classroom.exception.ForbiddenException;
import ru.classroom.exception.NotFoundException;
import ru.classroom.exception.UserNotFoundException;
import ru.classroom.repository.ClassroomMembershipRepository;
import ru.classroom.repository.ClassroomRepository;
import ru.classroom.repository.RoleRepository;
import ru.classroom.service.user.UserService;

import java.util.Optional;

@Service
public class ClassroomMembershipService {

    private static final String STUDENT_ROLE_CODE = "STUDENT";

    private final ClassroomMembershipRepository classroomMembershipRepository;
    private final ClassroomRepository classroomRepository;
    private final ClassroomService classroomService;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public ClassroomMembershipService(
            ClassroomMembershipRepository classroomMembershipRepository,
            ClassroomRepository classroomRepository,
            ClassroomService classroomService,
            RoleRepository roleRepository,
            UserService userService) {
        this.classroomMembershipRepository = classroomMembershipRepository;
        this.classroomRepository = classroomRepository;
        this.classroomService = classroomService;
        this.roleRepository = roleRepository;
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
     * Обновляет признак скрытия класса для участника.
     * @param classroomMembershipId идентификатор членства в классе
     * @param isHidden признак скрытия класса
     */
    @Transactional
    public void updateHidden(String classroomMembershipId, boolean isHidden) {
        ClassroomMembership membership = getClassroomMembership(classroomMembershipId);
        membership.setHidden(isHidden);
    }

    /**
     * Выполняет выход участника из класса.
     * @param classroomMembershipId идентификатор членства в классе
     * @return название класса, из которого вышел участник
     */
    @Transactional
    public NameClassroomResponse leaveClassroom(String classroomMembershipId) {
        ClassroomMembership membership = getClassroomMembership(classroomMembershipId);
        membership.setLeave(false);

        return new NameClassroomResponse(membership.getClassroom().getName());
    }

    /**
     * Подключает текущего пользователя к классу по коду приглашения.
     * @param inviteCode код для подключения к классу
     * @return информация о подключённом классе
     */
    @Transactional
    public NameClassroomResponse joinClassroom(String inviteCode) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException(
                        "Авторизованный пользователь не найден"
                ));

        Classroom classroom = classroomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException(
                        "Класс с таким кодом не найден"
                ));

        Optional<ClassroomMembership> existingMembership =
                classroomMembershipRepository.findByClassroomIdAndUserId(
                        classroom.getId(),
                        user.getId()
                );

        if (existingMembership.isPresent()) {
            ClassroomMembership membership = existingMembership.get();

            if (membership.isBlocked()) {
                throw new ConflictException("Вы не можете подключиться к этому классу");
            }

            if (membership.isLeave()) {
                membership.setLeave(false);
                classroomMembershipRepository.save(membership);
            } else {
                throw new ConflictException("Вы уже состоите в этом классе");
            }
        } else {
            Role studentRole = roleRepository.findByCode(STUDENT_ROLE_CODE)
                    .orElseThrow(() -> new IllegalStateException(
                            "Роль STUDENT не найдена"
                    ));

            String classroomMembershipId = classroomService.generateClassroomMembershipId();
            ClassroomMembership newMembership = new ClassroomMembership(
                    classroomMembershipId,
                    classroom,
                    user,
                    studentRole
            );

            classroomMembershipRepository.save(newMembership);
        }

        return new NameClassroomResponse(classroom.getName());
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
