package ru.classroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "classroom_colors")
public class ClassroomColor {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "hex_code", nullable = false, columnDefinition = "CHAR(7)")
    private String hexCode;

    @Column(name = "text_hex_code", nullable = false, columnDefinition = "CHAR(7)")
    private String textHexCode;

    protected ClassroomColor() {

    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getHexCode() {
        return hexCode;
    }

    public String getTextHexCode() {
        return textHexCode;
    }
}
