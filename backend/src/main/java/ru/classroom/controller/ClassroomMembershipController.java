package ru.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
}
