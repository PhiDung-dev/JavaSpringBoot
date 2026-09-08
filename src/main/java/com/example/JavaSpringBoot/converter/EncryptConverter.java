package com.example.JavaSpringBoot.converter;

import jakarta.persistence.AttributeConverter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class EncryptConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        if(Objects.isNull(s)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    @Override
    public String convertToEntityAttribute(String s) {
        if(Objects.isNull(s)) {
            return null;
        }
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }
}
