package com.ditrit.letomodelizerapi.model.file;

import jakarta.validation.constraints.NotBlank;

/**
 * A record representing a file.
 *
 * @param path the file path. Cannot be blank.
 * @param content the content of the file. Cannot be blank.
 */
public record FileRecord(
        @NotBlank String path,
        @NotBlank String content
) {
}
