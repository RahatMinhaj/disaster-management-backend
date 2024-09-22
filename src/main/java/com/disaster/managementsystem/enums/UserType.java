package com.disaster.managementsystem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum UserType {
    ADMIN(1, "ADMIN"),
    JUST_AN_USER(2, "JUST_AN_USER"),
    VOLUNTEER(3, "VOLUNTEER"),
    UNKNOWN(0, "UNKNOWN");

    private Integer id;
    private String status;

    UserType(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static UserType getById(Integer id) {
        for (UserType genderType : UserType.values()) {
            if (genderType.getId() == id) return genderType;
        }
        return null;
    }

    public static UserType getByStatus(String status) {
        for (UserType value : UserType.values()) {
            if (value.getStatus().equals(status)) return value;
        }
        return null;
    }

    @Converter
    public static class UserTypeConverter implements AttributeConverter<UserType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(UserType type) {
            return type != null ? type.getId() : UNKNOWN.id;
        }

        @Override
        public UserType convertToEntityAttribute(Integer id) {
            return id != null ? UserType.getById(id) : UNKNOWN;
        }
    }
}
