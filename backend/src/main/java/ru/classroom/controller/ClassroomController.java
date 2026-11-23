package ru.classroom.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.classroom.*;
import ru.classroom.dto.classroommembership.NameClassroomResponse;
import ru.classroom.entity.ClassroomMembership;
import ru.classroom.service.classroom.ClassroomColorService;
import ru.classroom.service.classroom.ClassroomMembershipService;
import ru.classroom.service.classroom.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomColorService classroomColorService;
    private final ClassroomService classroomService;

    public ClassroomController(
            ClassroomColorService classroomColorService,
            ClassroomService classroomService
    ) {
        this.classroomColorService = classroomColorService;
        this.classroomService = classroomService;
    }

    /**
     * Возвращает список всех доступных цветов учебных классов.
     * @return список цветов учебных классов
     */
    @GetMapping("/colors")
    public ResponseEntity<List<ClassroomColorResponse>> getAllColors() {
        return ResponseEntity.ok(
                classroomColorService.getAllClassroomColors()
        );
    }

    /**
     * Генерирует и возвращает уникальный код приглашения для нового класса.
     * @return DTO с кодом приглашения
     */
    @GetMapping("/invite-code")
    public ResponseEntity<InviteCodeResponse> generateInviteCode() {
        String inviteCode = classroomService.generateInviteCode();
        return ResponseEntity.ok(
                new InviteCodeResponse(inviteCode)
        );
    }

    /**
     * Создаёт новый учебный класс и добавляет текущего пользователя
     * в качестве его создателя.
     * @param request данные для создания учебного класса
     * @return информация о созданном классе
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NameClassroomResponse> createClassroom(@Valid @RequestBody CreateClassroomRequest request) {
        return ResponseEntity.ok(
                classroomService.createClassroom(request)
        );
    }

    /**
     * Архивирует класс.
     * @param classroomMembershipId идентификатор членства в классе
     * @return название архивированного класса
     */
    @PatchMapping("/{classroomMembershipId}/archive")
    public ResponseEntity<NameClassroomResponse> archiveClassroom(
            @PathVariable String classroomMembershipId
    ) {
        return ResponseEntity.ok(
                classroomService.updateArchiveClassroom(classroomMembershipId, true)
        );
    }

    /**
     * Восстанавливает класс из архива.
     * @param classroomMembershipId идентификатор членства в классе
     * @return название восстановленного класса
     */
    @PatchMapping("/{classroomMembershipId}/unarchive")
    public ResponseEntity<NameClassroomResponse> unarchiveClassroom(
            @PathVariable String classroomMembershipId
    ) {
        return ResponseEntity.ok(
                classroomService.updateArchiveClassroom(classroomMembershipId, false)
        );
    }

    /**
     * Получает список учебных классов текущего пользователя.
     * @param page номер страницы (начиная с 0)
     * @param size количество элементов на странице
     * @param isHidden фильтр по скрытым классам
     * @param isMyProjects показывать только созданные пользователем классы
     * @return страница с карточками учебных классов
     */
    @GetMapping
    public ResponseEntity<Page<ClassroomCardResponse>> getClassrooms(
            @RequestParam(defaultValue = "0")
            @Min(0) int page,

            @RequestParam(defaultValue = "15")
            @Min(1) @Max(15) int size,

            @RequestParam(defaultValue = "false")
            boolean isHidden,

            @RequestParam(defaultValue = "false")
            boolean isMyProjects,

            @RequestParam(required = false)
            String search
    ) {
        return ResponseEntity.ok(
                classroomService.getClassrooms(page, size, isHidden, isMyProjects, search)
        );
    }

    /**
     * Получает подробную информацию о классе по его идентификатору.
     * @param classroomId идентификатор класса
     * @return подробная информация о классе
     */
    @GetMapping("/{classroomId}")
    public ResponseEntity<DetailedClassroomResponse> getDetailedClassroom(@PathVariable String classroomId) {
        return ResponseEntity.ok(
                classroomService.getDetailedClassroom(classroomId)
        );
    }
}
