package com.ditrit.letomodelizerapi.model.ia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * A record representing a request to an Intelligent Assistant (IA) within the application.
 * This record includes details about the request such as the plugin involved, the type of request, and
 * a description of the request. It is used as a data transfer object to encapsulate the information needed
 * by the IA to process user requests.
 *
 * @param plugin the name of the plugin related to the IA request. Must not be blank.
 * @param type the type of the IA request, constrained to specific values such as "diagram" by a pattern
 *        to ensure that only valid request types are considered.
 * @param description a description of the IA request, providing context or additional information. Must not be blank.
 */
public record IARequestRecord(
        @NotBlank String plugin,
        @Pattern(regexp = "diagram") String type,
        @NotBlank String description
        ) {
}
