package ru.classroom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.classroom.dto.classroom.ClassroomColorResponse;
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
}
