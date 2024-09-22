package com.disaster.managementsystem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum Severity {
    LOW(1, "LOW"),
    MEDIUM(2, "MEDIUM"),
    ACUTE(3, "ACUTE"),
    UNKNOWN(0, "UNKNOWN");


    private Integer id;
    private String status;

    Severity(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static Severity getById(Integer id) {
        for (Severity genderType : Severity.values()) {
            if (genderType.getId() == id) return genderType;
        }
        return null;
    }

    public static Severity getByStatus(String status) {
        for (Severity value : Severity.values()) {
            if (value.getStatus().equals(status)) return value;
        }
        return null;
    }

    @Converter
    public static class SeverityConverter implements AttributeConverter<Severity, Integer> {
        @Override
        public Integer convertToDatabaseColumn(Severity type) {
            return type != null ? type.getId() : UNKNOWN.id;
        }

        @Override
        public Severity convertToEntityAttribute(Integer id) {
            return id != null ? Severity.getById(id) : UNKNOWN;
        }
    }
}
