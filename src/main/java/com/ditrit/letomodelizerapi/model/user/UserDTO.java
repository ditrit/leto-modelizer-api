package com.ditrit.letomodelizerapi.model.user;

import lombok.Data;

/**
 * Data Transfer Object of the user model, including only the essential fields.
 */
@Data
public class UserDTO {

    /**
     * Email of the user.
     */
    private String email;

    /**
     * Login of the user.
     */
    private String login;

    /**
     * Full name of the user.
     */
    private String name;
}
