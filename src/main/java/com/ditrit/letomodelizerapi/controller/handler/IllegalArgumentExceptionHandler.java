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

/**
 * Handle IllegalArgumentException on UUID error and send appropriate response.
 */
public class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(final IllegalArgumentException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ErrorDTO error = new ErrorDTO();
        error.setCode(ErrorType.WRONG_VALUE.getCode());
        error.setMessage(ErrorType.WRONG_VALUE.getMessage());
        error.setField("id");
        error.setCause(exception.getMessage());

        try {
            return Response.status(ErrorType.WRONG_VALUE.getStatus().value())
                    .entity(mapper.writeValueAsString(error))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (JsonProcessingException e) {
            throw new ClientErrorException(Response.Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
