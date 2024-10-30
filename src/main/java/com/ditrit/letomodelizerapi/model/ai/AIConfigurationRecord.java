package com.ditrit.letomodelizerapi.model.ai;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents an immutable AI configuration.
 * @param handler The configuration handler.
 * @param key The non-blank configuration key.
 * @param value The non-blank configuration value.
 */
public record AIConfigurationRecord(
        String handler,
        @NotBlank String key,
        @NotBlank String value
) {
}
