package com.ditrit.letomodelizerapi.model.csrf;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO) representing a user's CSRF (Cross-Site Request Forgery) token.
 * This class encapsulates the CSRF token details, including the header name where the token should be sent,
 * the token value itself, and the expiration date of the token. It provides a method to get the expiration date
 * as a long value, which is useful for serialization and client-side processing.
 */
@Data
public class UserCsrfTokenDTO {

    /**
     * The name of the HTTP header through which the CSRF token should be sent in requests.
     */
    private String headerName;

    /**
     * The value of the CSRF token.
     */
    private String token;

    /**
     * The expiration date and time of the CSRF token.
     */
    private Timestamp expirationDate;

    /**
     * Gets the expiration date of the CSRF token as a long value representing the time in milliseconds since
     * the epoch (January 1, 1970, 00:00:00 GMT).
     *
     * @return the expiration date of the CSRF token as milliseconds since the epoch.
     */
    @JsonGetter("expirationDate")
    public long getExpirationDateAsLong() {
        return expirationDate.getTime();
    }
}
