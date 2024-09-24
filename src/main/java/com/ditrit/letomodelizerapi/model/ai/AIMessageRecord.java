package com.ditrit.letomodelizerapi.model.ai;

import jakarta.validation.constraints.NotBlank;

/**
 * A record representing the data required to create a new conversation with AI.
 *
 * @param message Message to send to AI. Cannot be blank.
 * @param plugin The name of the plugin involved in the conversation. Cannot be blank.
 */
public record AIMessageRecord(
        @NotBlank String message,
        @NotBlank String plugin
) {
}
