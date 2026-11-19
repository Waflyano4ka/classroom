package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.classroom.dto.classroommembership.JoinClassroomRequest;
import ru.classroom.dto.classroommembership.JoinClassroomResponse;
import ru.classroom.dto.classroommembership.UpdateMembershipFavoriteRequest;
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
     * Подключает текущего пользователя к классу по коду приглашения.
     * @param request данные для подключения к классу
     * @return информация о подключённом классе
     */
    @PostMapping("/join")
    public ResponseEntity<JoinClassroomResponse> joinClassroom(@Valid @RequestBody JoinClassroomRequest request){
        return ResponseEntity.ok(
                classroomMembershipService.joinClassroom(request.getInviteCode())
        );
    }
}
