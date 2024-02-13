package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.library.LibraryTemplateType;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: JsonNodeToLibraryTemplateFunction")
class JsonNodeToLibraryTemplateFunctionTest {

    @Test
    @DisplayName("Test apply: should transform JsonNode with null to Library template")
    void testApplyWithNull() {
        JsonNodeToLibraryTemplateFunction mapper = new JsonNodeToLibraryTemplateFunction();

        LibraryTemplate expectedTemplate = new LibraryTemplate();

        expectedTemplate.setName("name");
        expectedTemplate.setType(LibraryTemplateType.PROJECT);
        expectedTemplate.setBasePath("basePath");
        expectedTemplate.setPlugins("[\"plugin\"]");
        expectedTemplate.setFiles(List.of("file1"));
        expectedTemplate.setSchemas(List.of());

        ArrayNode files = JsonNodeFactory.instance.arrayNode();
        files.add("file1");

        ArrayNode plugins = JsonNodeFactory.instance.arrayNode();
        plugins.add("plugin");

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("name", "name");
        json.put("type", "PROJECT");
        json.put("basePath", "basePath");
        json.set("files", files);
        json.set("plugins", plugins);

        assertEquals(expectedTemplate, mapper.apply(json));
    }

    @Test
    @DisplayName("Test apply: should transform JsonNode to Library")
    void testApply() {
        JsonNodeToLibraryTemplateFunction mapper = new JsonNodeToLibraryTemplateFunction();

        LibraryTemplate expectedTemplate = new LibraryTemplate();

        expectedTemplate.setName("name");
        expectedTemplate.setType(LibraryTemplateType.DIAGRAM);
        expectedTemplate.setBasePath("basePath");
        expectedTemplate.setDescription("description");
        expectedTemplate.setDocumentationUrl("documentationUrl");
        expectedTemplate.setIcon("icon");
        expectedTemplate.setPlugins("[\"plugin\"]");
        expectedTemplate.setFiles(List.of("file1"));
        expectedTemplate.setSchemas(List.of("schema1"));

        ArrayNode files = JsonNodeFactory.instance.arrayNode();
        files.add("file1");

        ArrayNode schemas = JsonNodeFactory.instance.arrayNode();
        schemas.add("schema1");

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("name", "name");
        json.put("type", "DIAGRAM");
        json.put("basePath", "basePath");
        json.put("plugin", "plugin");
        json.put("description", "description");
        json.put("documentationUrl", "documentationUrl");
        json.put("icon", "icon");
        json.set("files", files);
        json.set("schemas", schemas);

        assertEquals(expectedTemplate, mapper.apply(json));
    }
}
