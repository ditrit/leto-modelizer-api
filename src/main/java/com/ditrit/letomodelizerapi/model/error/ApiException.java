package com.ditrit.letomodelizerapi.model.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * Default application exception.
 */
@Getter
public class ApiException extends RuntimeException {

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = -6050355569225117225L;
    /**
     * HTTP status of exception.
     */
    private final HttpStatus status;
    /**
     * Error of exception.
     */
    private final ErrorDTO error;
    /**
     * Log exception.
     */
    private final boolean logIt;

    /**
     * Parameters error exception.
     *
     * @param error Error type.
     * @param field Field name.
     */
    public ApiException(final ErrorType error, final String field) {
        this(null, error, field, true);
    }

    /**
     * Parameters error exception.
     *
     * @param error Error type.
     * @param field Field name.
     * @param value Field value.
     */
    public ApiException(final ErrorType error, final String field, final String value) {
        this(null, error, field, value, true);
    }

    /**
     * Parameters error exception.
     *
     * @param exception The cause of this error.
     * @param error     Error type.
     * @param field     Field name.
     */
    public ApiException(final Throwable exception, final ErrorType error, final String field) {
        this(exception, error, field, null, true);
    }

    /**
     * Parameters error exception.
     *
     * @param exception The cause of this error.
     * @param error     Error type.
     * @param field     Field name.
     * @param value     Field value.
     */
    public ApiException(final Throwable exception, final ErrorType error, final String field, final String value) {
        this(exception, error, field, value, true);
    }

    /**
     * Parameters error exception.
     *
     * @param exception      The cause of this error.
     * @param error          Error type.
     * @param field          Field name.
     * @param needToBeLogged Indicates if exception need to be logged.
     */
    public ApiException(final Throwable exception, final ErrorType error, final String field,
                        final boolean needToBeLogged) {
        this(exception, error, field, null, needToBeLogged);
    }

    /**
     * Parameters error exception.
     *
     * @param exception      The cause of this error.
     * @param error          Error type.
     * @param field          Field name.
     * @param value          Field value.
     * @param needToBeLogged Indicates if exception need to be logged.
     */
    public ApiException(final Throwable exception, final ErrorType error, final String field, final String value,
                           final boolean needToBeLogged) {
        super(error.getMessage(), exception);
        this.status = error.getStatus();
        this.error = new ErrorDTO(error.getCode(), error.getMessage(), field, value, exception);
        this.logIt = needToBeLogged;
    }
}
