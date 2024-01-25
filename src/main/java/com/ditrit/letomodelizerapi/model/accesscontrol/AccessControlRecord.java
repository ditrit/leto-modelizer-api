package com.ditrit.letomodelizerapi.model.accesscontrol;

import jakarta.validation.constraints.NotBlank;

/**
 * Represents an immutable record for Access Control.
 *
 * @param name the non-blank name associated with the access control entity
 */
public record AccessControlRecord(@NotBlank String name) {
}
