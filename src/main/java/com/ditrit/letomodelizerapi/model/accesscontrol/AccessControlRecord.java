package com.ditrit.letomodelizerapi.model.accesscontrol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents an immutable record for Access Control.
 *
 * @param name the non-blank name associated with the access control entity
 */
public record AccessControlRecord(@NotBlank @Pattern(regexp = "^[A-Z0-9][A-Z0-9_-]+[A-Z0-9]$") String name) {
}
