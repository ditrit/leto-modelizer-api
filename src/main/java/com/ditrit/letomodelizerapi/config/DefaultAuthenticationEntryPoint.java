package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * The DefaultAuthenticationEntryPoint class implements the AuthenticationEntryPoint interface
 * to handle authentication errors in a custom manner. This class is designed to intercept
 * authentication failures and respond with a detailed JSON error response.
 *
 * This entry point is typically used in Spring Security configuration to manage authentication
 * error handling, especially to provide custom responses for authorization failures. It is used
 * in the .exceptionHandling() method to set a custom authentication entry point.
 *
 * @see AuthenticationEntryPoint
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        ErrorDTO error = new ErrorDTO(
            ErrorType.AUTHORIZATION_ERROR.getCode(),
            ErrorType.AUTHORIZATION_ERROR.getMessage(),
            null,
            null,
            authException
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(ErrorType.AUTHORIZATION_ERROR.getStatus().value());
        response.getWriter().write(mapper.writeValueAsString(error));
    }
}
