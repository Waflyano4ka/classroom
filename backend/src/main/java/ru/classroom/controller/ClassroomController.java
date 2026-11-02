package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.classroom.ClassroomCardResponse;
import ru.classroom.dto.classroom.ClassroomColorResponse;
import ru.classroom.dto.classroom.CreateClassroomRequest;
import ru.classroom.dto.classroom.InviteCodeResponse;
import ru.classroom.service.classroom.ClassroomColorService;
import ru.classroom.service.classroom.ClassroomService;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomColorService classroomColorService;
    private final ClassroomService classroomService;

    public ClassroomController(ClassroomColorService classroomColorService, ClassroomService classroomService) {
        this.classroomColorService = classroomColorService;
        this.classroomService = classroomService;
    }

    /**
     * Возвращает список всех доступных цветов учебных классов.
     * @return список цветов учебных классов
     */
    @GetMapping("/colors")
    public List<ClassroomColorResponse> getAllColors() {
        return classroomColorService.getAllClassroomColors();
    }

    /**
     * Генерирует и возвращает уникальный код приглашения для нового класса.
     * @return DTO с кодом приглашения
     */
    @GetMapping("/invite-code")
    public InviteCodeResponse generateInviteCode() {
        String inviteCode = classroomService.generateInviteCode();
        return new InviteCodeResponse(inviteCode);
    }

    /**
     * Создаёт новый учебный класс и добавляет текущего пользователя
     * в качестве его создателя.
     * @param request данные для создания учебного класса
     * @return информация о созданном классе
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassroomCardResponse createClassroom(@Valid @RequestBody CreateClassroomRequest request) {
        return classroomService.createClassroom(request);
    }
}
