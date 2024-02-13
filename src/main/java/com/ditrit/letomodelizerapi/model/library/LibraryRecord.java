package com.ditrit.letomodelizerapi.model.library;

import jakarta.validation.constraints.Pattern;

/**
 * A record representing a library entity with URL and role properties.
 * The URL must match a specific pattern, indicating it should end with "/index.json".
 *
 * @param url the URL of the library, must conform to the specified pattern.
 * @param role the role associated to the library.
 */
public record LibraryRecord(
        @Pattern(regexp = ".+/index\\.json$")
        String url,
        String role) {
}
