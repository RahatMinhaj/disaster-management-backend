package com.disaster.managementsystem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum StockType {

    STOCKED(1, "STOCKED"),

    DISTRIBUTED(2, "DISTRIBUTED"),

    UNKNOWN(0, "UNKNOWN");


    private Integer id;
    private String status;

    StockType(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static StockType getById(Integer id) {
        for (StockType genderType : StockType.values()) {
            if (genderType.getId() == id) return genderType;
        }
        return null;
    }

    public static StockType getByStatus(String status) {
        for (StockType value : StockType.values()) {
            if (value.getStatus().equals(status)) return value;
        }
        return null;
    }

    @Converter
    public static class StockTypeConverter implements AttributeConverter<StockType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(StockType type) {
            return type != null ? type.getId() : UNKNOWN.id;
        }

        @Override
        public StockType convertToEntityAttribute(Integer id) {
            return id != null ? StockType.getById(id) : UNKNOWN;
        }
    }
}
