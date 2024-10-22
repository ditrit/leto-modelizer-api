package com.ditrit.letomodelizerapi.model.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an immutable AI secret.
 * @param key The non-blank secret key.
 * @param value The non-blank secret value.
 */
public record AISecretRecord(
        @NotBlank @Pattern(regexp = "^[a-zA-Z-_]+$") String key,
        @NotBlank String value
) {
}
