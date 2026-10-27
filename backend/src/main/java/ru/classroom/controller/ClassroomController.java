package ru.classroom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.classroom.dto.classroom.ClassroomColorResponse;
import ru.classroom.service.classroom.ClassroomColorService;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomColorService classroomColorService;

    public ClassroomController(ClassroomColorService classroomColorService) {
        this.classroomColorService = classroomColorService;
    }

    @GetMapping("/colors")
    public List<ClassroomColorResponse> getAllColors() {
        return classroomColorService.getAllClassroomColors();
    }
}
