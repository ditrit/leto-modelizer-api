package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Handle api exception and send appropriate response.
 */
@Provider
@Slf4j
public class ApiExceptionHandler implements ExceptionMapper<ApiException> {

    @Override
    public final Response toResponse(final ApiException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        log.debug("General error", exception);

        try {
            return Response.status(exception.getStatus().value())
                    .entity(mapper.writeValueAsString(exception.getError()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            throw new ApiException(exception, ErrorType.INTERNAL_ERROR, exception.getError().getField());
        }
    }
}
