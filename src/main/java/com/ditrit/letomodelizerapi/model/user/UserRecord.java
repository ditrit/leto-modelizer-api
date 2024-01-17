package com.ditrit.letomodelizerapi.model.user;

/**
 * Represents a record of a user.
 * @param email Email of the user.
 * @param login Login of the user.
 * @param name Full name of the user.
 * @param picture URL link to the user's profile picture.
 */
public record UserRecord(
    String email,
    String login,
    String name,
    String picture
) {
}
