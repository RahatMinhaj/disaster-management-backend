package com.disaster.managementsystem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum Status {
    SUBMITTED(1, "SUBMITTED"),
    PENDING(2, "PENDING"),
    RESOLVED(3, "RESOLVED"),
    IN_PROCESS(4, "IN_PROCESS"),
    APPROVED(5, "APPROVED"),
    BANNED(6, "BANNED"),
    UNKNOWN(0, "UNKNOWN");


    private Integer id;
    private String status;

    Status(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static Status getById(Integer id) {
        for (Status genderType : Status.values()) {
            if (genderType.getId() == id) return genderType;
        }
        return null;
    }

    public static Status getByStatus(String status) {
        for (Status value : Status.values()) {
            if (value.getStatus().equals(status)) return value;
        }
        return null;
    }

    @Converter
    public static class StatusConverter implements AttributeConverter<Status, Integer> {
        @Override
        public Integer convertToDatabaseColumn(Status type) {
            return type != null ? type.getId() : UNKNOWN.id;
        }

        @Override
        public Status convertToEntityAttribute(Integer id) {
            return id != null ? Status.getById(id) : UNKNOWN;
        }
    }
}
