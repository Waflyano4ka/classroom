package ru.classroom.service.classroom;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.constant.RoleCode;
import ru.classroom.dto.ClassroomWithCreatorProjection;
import ru.classroom.dto.classroom.ClassroomCardResponse;
import ru.classroom.dto.classroom.CreateClassroomRequest;
import ru.classroom.dto.classroommembership.NameClassroomResponse;
import ru.classroom.entity.*;
import ru.classroom.exception.ConflictException;
import ru.classroom.exception.UserNotFoundException;
import ru.classroom.repository.ClassroomColorRepository;
import ru.classroom.repository.ClassroomMembershipRepository;
import ru.classroom.repository.ClassroomRepository;
import ru.classroom.repository.RoleRepository;
import ru.classroom.service.user.UserService;
import ru.classroom.util.ClassroomCodeGenerator;
import ru.classroom.util.IdGenerator;

@Service
public class ClassroomService {

    private static final String CLASSROOM_ID_PREFIX = "class-";
    private static final String CLASSROOM_MEMBERSHIP_ID_PREFIX = "member-";

    private final ClassroomMembershipAccessService classroomMembershipAccessService;
    private final ClassroomMembershipRepository classroomMembershipRepository;
    private final ClassroomColorRepository classroomColorRepository;
    private final ClassroomCodeGenerator classroomCodeGenerator;
    private final ClassroomRepository classroomRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final IdGenerator idGenerator;

    public ClassroomService(
            ClassroomMembershipAccessService classroomMembershipAccessService,
            ClassroomMembershipRepository classroomMembershipRepository,
            ClassroomColorRepository classroomColorRepository,
            ClassroomCodeGenerator classroomCodeGenerator,
            ClassroomRepository classroomRepository,
            RoleRepository roleRepository,
            UserService userService,
            IdGenerator idGenerator
    ) {
        this.classroomMembershipAccessService = classroomMembershipAccessService;
        this.classroomMembershipRepository = classroomMembershipRepository;
        this.classroomColorRepository = classroomColorRepository;
        this.classroomCodeGenerator = classroomCodeGenerator;
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
     * @throws IllegalStateException     если роль создателя не найдена
     * @throws ConflictException         Код приглашения уже используется
     */
    @Transactional
    public NameClassroomResponse createClassroom(CreateClassroomRequest request) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException(
                        "Авторизованный пользователь не найден"
                ));

        ClassroomColor color = classroomColorRepository.findById(request.getColorId())
                .orElseGet(() -> classroomColorRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "В системе отсутствуют доступные цвета классов"
                        )));

        Role creatorRole = roleRepository.findByCode(RoleCode.CREATOR.getCode())
                .orElseThrow(() -> new IllegalStateException(
                        "Роль CREATOR не найдена"
                ));

        if (classroomRepository.existsByInviteCode(request.getInviteCode())) {
            throw new ConflictException("Этот код уже используется");
        }

        String classroomId = generateClassroomId();
        Classroom classroom = new Classroom(
                classroomId,
                request.getName(),
                color,
                request.getInviteCode()
        );

        Classroom savedClassroom = classroomRepository.save(classroom);

        String classroomMembershipId = generateClassroomMembershipId();
        ClassroomMembership membership = new ClassroomMembership(
                classroomMembershipId,
                savedClassroom,
                user,
                creatorRole
        );

        return new NameClassroomResponse(savedClassroom.getName());
    }

    /**
     * Получает список учебных классов текущего пользователя с пагинацией,
     * фильтрацией и сортировкой.
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @param isHidden фильтр по скрытым классам
     * @param isMyProjects фильтр по созданным классам
     * @return страница с карточками учебных классов
     */
    @Transactional(readOnly = true)
    public Page<ClassroomCardResponse> getClassrooms(
            int page,
            int size,
            boolean isHidden,
            boolean isMyProjects,
            String search
    ) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException(
                        "Авторизованный пользователь не найден"
                ));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("favorite"),
                        Sort.Order.asc("classroom.name")
                )
        );

        String normalizedSearch = search == null ? "" : search.trim();
        return classroomMembershipRepository.findAllByUserId(
                        user.getId(),
                        isHidden,
                        isMyProjects,
                        RoleCode.CREATOR.getCode(),
                        false,
                        normalizedSearch,
                        pageable
                )
                .map(this::toClassroomCardResponse);
    }

    /**
     * Генерирует уникальный код приглашения, проверяя его отсутствие в базе данных.
     * @return уникальный код приглашения
     */
    public String generateInviteCode() {
        String inviteCode;

        do {
            inviteCode = classroomCodeGenerator.generate();
        } while (classroomRepository.existsByInviteCode(inviteCode));

        return inviteCode;
    }

    /**
     * Генерирует уникальный идентификатор участия пользователя в классе.
     * @return уникальный идентификатор участия пользователя
     */
    public String generateClassroomMembershipId() {
        String classroomMembershipId;
        do {
            classroomMembershipId = CLASSROOM_MEMBERSHIP_ID_PREFIX + idGenerator.generate();
        } while (classroomMembershipRepository.existsById(classroomMembershipId));

        return classroomMembershipId;
    }

    /**
     * Обновляет статус архивации класса.
     * @param classroomMembershipId идентификатор членства в классе
     * @param isArchived признак архивации класса
     * @return название класса
     * @throws AccessDeniedException если участник не является создателем класса
     */
    @Transactional
    public NameClassroomResponse updateArchiveClassroom(String classroomMembershipId, boolean isArchived) {
        ClassroomMembership membership = classroomMembershipAccessService.getClassroomMembershipRole(
                classroomMembershipId,
                RoleCode.CREATOR.getCode()
        );

        Classroom classroom = membership.getClassroom();
        classroom.setArchived(isArchived);

        return new NameClassroomResponse(classroom.getName());
    }

    /**
     * Преобразует сущность класса в DTO карточки класса.
     * @param classroomWithCreatorProjection сущность участия пользователя в классе и создателя
     * @return DTO карточки класса
     */
    private ClassroomCardResponse toClassroomCardResponse(ClassroomWithCreatorProjection classroomWithCreatorProjection) {
        ClassroomMembership membership = classroomWithCreatorProjection.getMembership();
        Classroom classroom = membership.getClassroom();
        Role role = membership.getRole();
        User creator = classroomWithCreatorProjection.getCreator();
        return new ClassroomCardResponse(
                membership.getId(),
                classroom.getId(),
                classroom.getName(),
                classroom.getColor().getHexCode(),
                classroom.getColor().getTextHexCode(),
                role.getCode(),
                creator.getLastName(),
                creator.getFirstName(),
                creator.getMiddleName(),
                creator.getUsername(),
                membership.isFavorite(),
                membership.isHidden(),
                classroom.isArchived()
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
}
