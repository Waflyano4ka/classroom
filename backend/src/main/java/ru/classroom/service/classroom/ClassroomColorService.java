package ru.classroom.service.classroom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.classroom.dto.classroom.ClassroomColorResponse;
import ru.classroom.entity.ClassroomColor;
import ru.classroom.repository.ClassroomColorRepository;

import java.util.List;

@Service
public class ClassroomColorService {

    private final ClassroomColorRepository classroomColorRepository;

    public ClassroomColorService(ClassroomColorRepository classroomColorRepository) {
        this.classroomColorRepository = classroomColorRepository;
    }

    /**
     * Возвращает список всех цветов учебных классов,
     * отсортированный по идентификатору.
     * @return список цветов учебных классов
     */
    @Transactional(readOnly = true)
    public List<ClassroomColorResponse> getAllClassroomColors() {
        return classroomColorRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::toClassroomColorResponse)
                .toList();
    }

    /**
     * Преобразует сущность цвета учебного класса в DTO ответа.
     * @param classroomColor сущность цвета учебного класса
     * @return DTO с данными цвета учебного класса
     */
    private ClassroomColorResponse toClassroomColorResponse(ClassroomColor classroomColor) {
        return new ClassroomColorResponse(
                classroomColor.getId(),
                classroomColor.getCode(),
                classroomColor.getName(),
                classroomColor.getHexCode().trim(),
                classroomColor.getTextHexCode().trim()
        );
    }
}
