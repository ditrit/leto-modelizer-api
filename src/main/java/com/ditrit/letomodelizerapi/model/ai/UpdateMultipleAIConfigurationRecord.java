package com.ditrit.letomodelizerapi.model.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Represents an immutable AI configuration to update.
 * @param id The configuration id.
 * @param handler The configuration handler.
 * @param key The non-blank configuration key.
 * @param value The non-blank configuration value.
 */
public record UpdateMultipleAIConfigurationRecord(
        @NotNull UUID id,
        String handler,
        @NotBlank String key,
        @NotBlank String value
) {
}
