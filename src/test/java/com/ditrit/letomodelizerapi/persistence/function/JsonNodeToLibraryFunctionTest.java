package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: JsonNodeToLibraryFunction")
class JsonNodeToLibraryFunctionTest {

    @Test
    @DisplayName("Test apply: should transform JsonNode with null to Library")
    void testApplyWithNull() {
        JsonNodeToLibraryFunction mapper = new JsonNodeToLibraryFunction();

        Library expectedLibrary = new Library();

        expectedLibrary.setName("name");
        expectedLibrary.setVersion("version");
        expectedLibrary.setMaintainer("maintainer");
        expectedLibrary.setDescription("description");

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("name", "name");
        json.put("version", "version");
        json.put("maintainer", "maintainer");
        json.put("description", "description");

        assertEquals(expectedLibrary, mapper.apply(json));
    }

    @Test
    @DisplayName("Test apply: should transform JsonNode to Library")
    void testApply() {
        JsonNodeToLibraryFunction mapper = new JsonNodeToLibraryFunction();

        Library expectedLibrary = new Library();

        expectedLibrary.setName("name");
        expectedLibrary.setVersion("version");
        expectedLibrary.setMaintainer("maintainer");
        expectedLibrary.setDescription("description");
        expectedLibrary.setDocumentationUrl("documentationUrl");
        expectedLibrary.setIcon("icon");

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("name", "name");
        json.put("version", "version");
        json.put("maintainer", "maintainer");
        json.put("description", "description");
        json.put("documentationUrl", "documentationUrl");
        json.put("icon", "icon");

        assertEquals(expectedLibrary, mapper.apply(json));
    }
}
