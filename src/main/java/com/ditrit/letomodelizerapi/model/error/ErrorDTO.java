package com.ditrit.letomodelizerapi.model.error;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Model of error.
 */
@Data
public class ErrorDTO implements Serializable {

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 65949594742461639L;
    /**
     * Error code.
     */
    private int code = -1;
    /**
     * Error message.
     */
    private String message;
    /**
     * Field name.
     */
    private String field;
    /**
     * Field value.
     */
    private String value;

    /**
     * Cause of the error.
     */
    private String cause;

    /**
     * Default constructor.
     */
    public ErrorDTO() {
    }

    /**
     * Constructor that init all member.
     *
     * @param code    Error code.
     * @param message Error message.
     * @param field   Error field.
     * @param value   Error value.
     * @param cause   Error exception Cause.
     */
    public ErrorDTO(final int code,
                    final String message,
                    final String field,
                    final String value,
                    final Throwable cause) {
        this.setCode(code);
        this.setMessage(message);
        this.setField(field);
        this.setValue(value);
        this.setThrowable(cause);
    }

    /**
     * Set the cause of the error.
     *
     * @param throwable Message.
     */
    public void setThrowable(final Throwable throwable) {
        String messageCause = null;
        if (throwable != null) {
            messageCause = throwable.getMessage();
        }

        this.setCause(messageCause);
    }
}
