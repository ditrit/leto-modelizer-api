package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Exception handler for handling {@link DataIntegrityViolationException}.
 * This class implements the {@link ExceptionMapper} interface to provide a custom response
 * when a {@link DataIntegrityViolationException} is thrown in the application.
 * It maps the exception to a structured error response in JSON format.
 */
public class DataIntegrityViolationExceptionHandler implements ExceptionMapper<DataIntegrityViolationException> {

    @Override
    public final Response toResponse(final DataIntegrityViolationException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        int status  = ErrorType.WRONG_VALUE.getStatus().value();
        ErrorDTO error = new ErrorDTO();
        error.setCode(ErrorType.WRONG_VALUE.getCode());
        error.setMessage(ErrorType.WRONG_VALUE.getMessage());

        if (exception.contains(ConstraintViolationException.class)) {
            ConstraintViolationException violation = (ConstraintViolationException) exception.getCause();
            error.setField("constraint");
            error.setValue(violation.getConstraintName());
            error.setCause(violation.getSQLException().getMessage());
        }

        try {
            return Response.status(status).entity(mapper.writeValueAsString(error))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (JsonProcessingException e) {
            throw new ClientErrorException(Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
