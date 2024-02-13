package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.library.LibraryTemplateType;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * A function that converts a JsonNode into a LibraryTemplate object.
 * It extracts fields such as name, type, basePath, files, description, documentation URL, icon, schemas,
 * and plugins from the JsonNode. Depending on the template type, it processes plugins differently.
 *
 * <p>For a PROJECT type, it directly assigns the "plugins" JSON as a string. For other types, it formats
 * a single plugin value into a JSON array string.
 *
 * <p>This function is designed for deserializing JSON data
 * into LibraryTemplate objects, ensuring all necessary information is captured and properly formatted
 * based on the template's type.
 */
public class JsonNodeToLibraryTemplateFunction implements Function<JsonNode, LibraryTemplate> {

    @Override
    public LibraryTemplate apply(final JsonNode json) {
        List<String> files = StreamSupport.stream(json.get("files").spliterator(), false)
                .map(JsonNode::asText).toList();
        LibraryTemplate template = new LibraryTemplate();

        template.setName(json.get("name").asText());
        template.setType(LibraryTemplateType.valueOf(json.get("type").asText()));
        template.setBasePath(json.get("basePath").asText());
        template.setFiles(files);

        if (json.hasNonNull("description")) {
            template.setDescription(json.get("description").asText());
        }

        if (json.hasNonNull("documentationUrl")) {
            template.setDocumentationUrl(json.get("documentationUrl").asText());
        }

        if (json.hasNonNull("icon")) {
            template.setIcon(json.get("icon").asText());
        }

        if (json.hasNonNull("schemas")) {
            List<String> schemas = StreamSupport.stream(json.get("schemas").spliterator(), false)
                    .map(JsonNode::asText).toList();
            template.setSchemas(schemas);
        } else {
            template.setSchemas(List.of());
        }

        if ("PROJECT".equals(template.getType().name())) {
            template.setPlugins(json.get("plugins").toString());
        } else {
            template.setPlugins(String.format("[\"%s\"]", json.get("plugin").asText()));
        }

        return template;
    }
}
