package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

/**
 * A function that converts a JsonNode into a Library object.
 * It extracts fields such as name, version, maintainer, description, icon (if present),
 * and documentation URL (if present) from the JsonNode and populates a new Library object with these values.
 * This function is useful for deserializing JSON data into Library objects.
 */
public class JsonNodeToLibraryFunction implements Function<JsonNode, Library> {

    /**
     * Library to set.
     */
    private Library fromLibrary;

    /**
     * Default constructor.
     */
    public JsonNodeToLibraryFunction()  {
        this(null);
    }

    /**
     * Provide a library to use it as default value.
     * All value of this library can be upgraded by values in json.
     * @param fromLibrary Library to use as default.
     */
    public JsonNodeToLibraryFunction(final Library fromLibrary) {
        this.fromLibrary = fromLibrary;
    }

    @Override
    public Library apply(final JsonNode json) {
        Library library = null;

        if (fromLibrary != null) {
            library = fromLibrary;
        } else {
            library = new Library();
        }

        library.setName(json.get("name").asText());
        library.setVersion(json.get("version").asText());
        library.setMaintainer(json.get("maintainer").asText());
        library.setDescription(json.get("description").asText());

        if (json.hasNonNull("icon")) {
            library.setIcon(json.get("icon").asText());
        }

        if (json.hasNonNull("documentationUrl")) {
            library.setDocumentationUrl(json.get("documentationUrl").asText());
        }

        return library;
    }
}
