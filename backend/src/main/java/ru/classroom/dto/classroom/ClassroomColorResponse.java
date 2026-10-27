package ru.classroom.dto.classroom;

public class ClassroomColorResponse {

    private final long id;
    private final String code;
    private final String name;
    private final String hexCode;
    private final String textHexCode;

    public ClassroomColorResponse(
            long id,
            String code,
            String name,
            String hexCode,
            String textHexCode
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.hexCode = hexCode;
        this.textHexCode = textHexCode;
    }

    public long getId() {
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
