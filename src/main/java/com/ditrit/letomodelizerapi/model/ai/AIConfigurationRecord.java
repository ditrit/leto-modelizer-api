package com.ditrit.letomodelizerapi.model.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an immutable AI configuration.
 * @param handler The configuration handler.
 * @param key The non-blank configuration key.
 * @param value The non-blank configuration value.
 */
public record AIConfigurationRecord(
        String handler,
        @NotBlank @Pattern(regexp = "^[a-zA-Z-_]+$") String key,
        @NotBlank String value
) {
}
