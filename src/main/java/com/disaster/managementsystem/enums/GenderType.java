package com.disaster.managementsystem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum GenderType {
    MALE(1, "Male"),
    FEMALE(2, "Female"),
    THIRD_GENDER(3, "THIRD_GENDER"),
    UNKNOWN(0, "UNKNOWN");


    private Integer id;
    private String status;

    GenderType(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static GenderType getById(Integer id) {
        for (GenderType genderType : GenderType.values()) {
            if (genderType.getId() == id) return genderType;
        }
        return null;
    }

    public static GenderType getByStatus(String status) {
        for (GenderType value : GenderType.values()) {
            if (value.getStatus().equals(status)) return value;
        }
        return null;
    }

    @Converter
    public static class GenderTypeConverter implements AttributeConverter<GenderType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(GenderType type) {
            return type != null ? type.getId() : UNKNOWN.id;
        }

        @Override
        public GenderType convertToEntityAttribute(Integer id) {
            return id != null ? GenderType.getById(id) : UNKNOWN;
        }
    }
}
