package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.classroommembership.JoinClassroomRequest;
import ru.classroom.dto.classroommembership.NameClassroomResponse;
import ru.classroom.dto.classroommembership.UpdateMembershipFavoriteRequest;
import ru.classroom.dto.classroommembership.UpdateMembershipHiddenRequest;
import ru.classroom.service.classroom.ClassroomMembershipService;

@RestController
@RequestMapping("/api/classroom-memberships")
public class ClassroomMembershipController {

    private final ClassroomMembershipService classroomMembershipService;

    public ClassroomMembershipController(ClassroomMembershipService classroomMembershipService) {
        this.classroomMembershipService = classroomMembershipService;
    }

    /**
     * Изменяет состояние избранного у учебного класса.
     * @param classroomMembershipId идентификатор членства в учебном классе
     * @param request новое состояние избранного
     * @return ответ без содержимого
     */
    @PatchMapping("/{classroomMembershipId}/favorite")
    public ResponseEntity<Void> updateFavorite(
            @PathVariable String classroomMembershipId,
            @Valid @RequestBody UpdateMembershipFavoriteRequest request
    ) {
        classroomMembershipService.updateFavorite(classroomMembershipId, request.getFavorite());
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновляет признак скрытия класса для участника.
     * @param classroomMembershipId идентификатор членства в классе
     * @param request данные для обновления признака скрытия
     * @return ответ без содержимого
     */
    @PatchMapping("/{classroomMembershipId}/hide")
    public ResponseEntity<Void> hideClassroom(
            @PathVariable String classroomMembershipId,
            @Valid @RequestBody UpdateMembershipHiddenRequest request
    ) {
        classroomMembershipService.updateHidden(classroomMembershipId, request.getHidden());
        return ResponseEntity.noContent().build();
    }

    /**
     * Выполняет выход участника из класса.
     * @param classroomMembershipId идентификатор членства в классе
     * @return название класса, из которого вышел участник
     */
    @PatchMapping("/{classroomMembershipId}/leave")
    public ResponseEntity<NameClassroomResponse> leaveClassroom(
            @PathVariable String classroomMembershipId
    ) {
        return ResponseEntity.ok(
                classroomMembershipService.leaveClassroom(classroomMembershipId)
        );
    }

    /**
     * Подключает текущего пользователя к классу по коду приглашения.
     * @param request данные для подключения к классу
     * @return информация о подключённом классе
     */
    @PostMapping("/join")
    public ResponseEntity<NameClassroomResponse> joinClassroom(@Valid @RequestBody JoinClassroomRequest request){
        return ResponseEntity.ok(
                classroomMembershipService.joinClassroom(request.getInviteCode())
        );
    }
}
