package com.nkhan.configuration.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanToYnConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return Boolean.TRUE.equals(value) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equalsIgnoreCase(value);
    }
}
