package com.ditrit.letomodelizerapi.model.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * List of controller exception error.
 */
@Getter
public enum ErrorType {
    /**
     * Error to call when authorization request is bad.
     */
    AUTHORIZATION_ERROR(101, "Authentication error.", HttpStatus.UNAUTHORIZED),
    /**
     * Error to call when user has bad authentication token.
     */
    AUTHORIZATION_FAILED(102, "Authentication has failed.", HttpStatus.UNAUTHORIZED),
    /**
     * Error to call when user has invalid permission(s).
     */
    NO_VALID_PERMISSION(103, "Missing valid permission for this action.", HttpStatus.FORBIDDEN),
    /**
     * Error to call when a field value is empty.
     */
    EMPTY_VALUE(201, "Field value is empty.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when field value contains a wrong filter value.
     */
    WRONG_FILTER_VALUE(202, "Field value is wrong.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when operator of wrong filter is wrong.
     */
    WRONG_FILTER_OPERATOR(203, "Field contains a wrong operator.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when entity is not found in database.
     */
    ENTITY_NOT_FOUND(204, "Entity not found.", HttpStatus.NOT_FOUND),
    /**
     * Error to call when there are problems with field.
     */
    WRONG_FIELD(205, "Somethings is wrong with this field.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when field value is wrong.
     */
    WRONG_VALUE(206, "Wrong field value.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when field is not an attribute of model.
     */
    UNKNOWN_FIELD(207, "Field is not an attribute of model.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when entity is not found in database.
     */
    ENTITY_ALREADY_EXISTS(208, "Entity already exists.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when an internal error occurred.
     */
    INTERNAL_ERROR(301, "Internal error occurred, please contact your administrator.",
        HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * Error to call when url of library is not in the whitelist.
     */
    UNAUTHORIZED_LIBRARY_URL(209, "Url of library is unauthorized.", HttpStatus.BAD_REQUEST),
    /**
     * Error to call when json of library is invalid.
     */
    WRONG_LIBRARY_VALUE(210, "Index.json of library is invalid.", HttpStatus.BAD_REQUEST);

    /**
     * Error code.
     */
    private final int code;

    /**
     * Exception message.
     */
    private final String message;

    /**
     * Exception status.
     */
    private final HttpStatus status;

    /**
     * Error type with message.
     *
     * @param code    Error code.
     * @param message Error message.
     * @param status  Error status.
     */
    ErrorType(final int code, final String message, final HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
