package com.ditrit.letomodelizerapi.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
@Slf4j
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(final List<String> attribute) {
        ArrayNode dbData = JsonNodeFactory.instance.arrayNode();

        if (attribute != null) {
            attribute.forEach(dbData::add);
        }

        return dbData.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(final String dbData) {
        List<String> attribute = new ArrayList<>();

        if (dbData == null) {
            return attribute;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = mapper.readTree(dbData);
            json.forEach(value -> attribute.add(value.asText()));
        } catch (JsonProcessingException e) {
            log.warn("Error when processing template attribute", e);
        }

        return attribute;
    }
}
