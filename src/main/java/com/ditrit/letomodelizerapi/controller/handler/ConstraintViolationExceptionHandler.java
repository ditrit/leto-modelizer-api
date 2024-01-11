package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Iterator;
import java.util.Optional;

/**
 * Handle validation exception and send appropriate response.
 */
@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public final Response toResponse(final ConstraintViolationException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Optional<ConstraintViolation<?>> opt = exception.getConstraintViolations().stream().findFirst();

        if (opt.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ConstraintViolation<?> violation = opt.get();
        ErrorDTO error = new ErrorDTO();
        int status;

        error.setField(getLastPath(violation.getPropertyPath()));

        if (violation.getInvalidValue() == null) {
            status = ErrorType.EMPTY_VALUE.getStatus().value();
            error.setCode(ErrorType.EMPTY_VALUE.getCode());
            error.setMessage(ErrorType.EMPTY_VALUE.getMessage());
        } else {
            status = ErrorType.WRONG_VALUE.getStatus().value();
            error.setCode(ErrorType.WRONG_VALUE.getCode());
            error.setMessage(ErrorType.WRONG_VALUE.getMessage());
            error.setValue(violation.getInvalidValue().toString());
        }

        try {
            return Response.status(status).entity(mapper.writeValueAsString(error))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (JsonProcessingException e) {
            throw new ClientErrorException(Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Retrieves the last node's name in the given Path.
     *  <p>
     *  This method iterates through the entire Path, returning the name of the last node in the sequence.
     *  </p>
     *  @param path The Path object to be processed.
     *  @return The name of the last node in the Path.
     */
    public String getLastPath(final Path path) {
        Iterator<Path.Node> iterator = path.iterator();
        Path.Node node = iterator.next();

        while (iterator.hasNext()) {
            node = iterator.next();
        }
        return node.getName();
    }
}
