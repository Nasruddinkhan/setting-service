package com.nkhan.configuration.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@Converter
public class StringSetToJsonConverter implements AttributeConverter<Set<String>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<LinkedHashSet<String>> STRING_SET_TYPE = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Unable to convert attribute values to JSON", ex);
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(dbData, STRING_SET_TYPE);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to convert JSON to attribute values", ex);
        }
    }
}
