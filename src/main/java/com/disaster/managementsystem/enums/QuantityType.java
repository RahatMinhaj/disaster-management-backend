package com.disaster.managementsystem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum QuantityType {
    KG(1, "KG"),
    PIECE(2, "PIECE"),
    TON(3, "TON"),
    UNKNOWN(0, "UNKNOWN");


    private Integer id;
    private String status;

    QuantityType(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static QuantityType getById(Integer id) {
        for (QuantityType genderType : QuantityType.values()) {
            if (genderType.getId() == id) return genderType;
        }
        return null;
    }

    public static QuantityType getByStatus(String status) {
        for (QuantityType value : QuantityType.values()) {
            if (value.getStatus().equals(status)) return value;
        }
        return null;
    }

    @Converter
    public static class QuantityTypeConverter implements AttributeConverter<QuantityType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(QuantityType type) {
            return type != null ? type.getId() : UNKNOWN.id;
        }

        @Override
        public QuantityType convertToEntityAttribute(Integer id) {
            return id != null ? QuantityType.getById(id) : UNKNOWN;
        }
    }
}
