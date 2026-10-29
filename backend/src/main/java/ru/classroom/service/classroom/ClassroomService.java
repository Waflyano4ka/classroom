package ru.classroom.service.classroom;

import org.apache.tomcat.util.http.InvalidParameterException;
import org.springframework.stereotype.Service;
import ru.classroom.dto.classroom.ClassroomCardResponse;
import ru.classroom.dto.classroom.CreateClassroomRequest;
import ru.classroom.entity.*;
import ru.classroom.repository.ClassroomColorRepository;
import ru.classroom.repository.ClassroomMembershipRepository;
import ru.classroom.repository.ClassroomRepository;
import ru.classroom.repository.RoleRepository;
import ru.classroom.service.user.UserService;
import ru.classroom.util.IdGenerator;

@Service
public class ClassroomService {

    private static final String CLASSROOM_ID_PREFIX = "class-";
    private static final String CLASSROOM_MEMBERSHIP_ID_PREFIX = "member-";
    private static final String CREATOR_ROLE_CODE = "CREATOR";

    private final ClassroomColorRepository classroomColorRepository;
    private final ClassroomMembershipRepository classroomMembershipRepository;
    private final ClassroomRepository classroomRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final IdGenerator idGenerator;

    public ClassroomService(
            ClassroomColorRepository classroomColorRepository,
            ClassroomMembershipRepository classroomMembershipRepository,
            ClassroomRepository classroomRepository,
            RoleRepository roleRepository,
            UserService userService,
            IdGenerator idGenerator
    ) {
        this.classroomColorRepository = classroomColorRepository;
        this.classroomMembershipRepository = classroomMembershipRepository;
        this.classroomRepository = classroomRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.idGenerator = idGenerator;
    }

    /**
     * Создаёт класс и добавляет текущего пользователя в качестве создателя.
     * @param request данные для создания класса
     * @return данные созданного класса для отображения карточки
     * @throws IllegalStateException     если авторизованный пользователь не найден
     * @throws InvalidParameterException если указан несуществующий цвет класса
     * @throws IllegalStateException     если роль создателя не найдена
     */
    public ClassroomCardResponse createClassroom(CreateClassroomRequest request) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException(
                        "Авторизованный пользователь не найден"
                ));

        ClassroomColor color = classroomColorRepository.findById(request.getColorId())
                .orElseThrow(() -> new InvalidParameterException(
                        "Указан некорректный цвет"
                ));

        Role creatorRole = roleRepository.findByCode(CREATOR_ROLE_CODE)
                .orElseThrow(() -> new IllegalStateException(
                        "Роль CREATOR не найдена"
                ));

        String classroomId = generateClassroomId();
        Classroom classroom = new Classroom(
                classroomId,
                request.getName(),
                color
        );

        Classroom savedClassroom = classroomRepository.save(classroom);

        String classroomMembershipId = generateClassroomMembershipId();
        ClassroomMembership membership = new ClassroomMembership(
                classroomMembershipId,
                classroom,
                user,
                creatorRole
        );

        ClassroomMembership savedMembership = classroomMembershipRepository.save(membership);

        return toClassroomCardResponse(savedMembership);
    }

    /**
     * Преобразует сущность класса в DTO карточки класса.
     * @param classroomMembership сущность участия пользователя в классе
     * @return DTO карточки класса
     */
    private ClassroomCardResponse toClassroomCardResponse(ClassroomMembership classroomMembership) {
        Classroom classroom = classroomMembership.getClassroom();
        Role role = classroomMembership.getRole();
        return new ClassroomCardResponse(
                classroom.getId(),
                classroom.getName(),
                classroom.getColor().getHexCode(),
                classroom.getColor().getTextHexCode(),
                classroom.isArchived(),
                role.getCode()
        );
    }

    /**
     * Генерирует уникальный идентификатор класса с заданным префиксом.
     * @return уникальный идентификатор класса
     */
    private String generateClassroomId() {
        String classroomId;
        do {
            classroomId = CLASSROOM_ID_PREFIX + idGenerator.generate();
        } while (classroomRepository.existsById(classroomId));

        return classroomId;
    }

    /**
     * Генерирует уникальный идентификатор участия пользователя в классе.
     * @return уникальный идентификатор участия пользователя
     */
    private String generateClassroomMembershipId() {
        String classroomMembershipId;
        do {
            classroomMembershipId = CLASSROOM_MEMBERSHIP_ID_PREFIX + idGenerator.generate();
        } while (classroomMembershipRepository.existsById(classroomMembershipId));

        return classroomMembershipId;
    }
}
