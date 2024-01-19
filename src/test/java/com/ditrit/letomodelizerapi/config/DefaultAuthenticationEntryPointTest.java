package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.model.error.ErrorType;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Tag("unit")
@DisplayName("Test class: DefaultAuthenticationEntryPoint")
class DefaultAuthenticationEntryPointTest {

    @Test
    @DisplayName("Test commence, should set valid response.")
    void testCommence() throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        int expectedStatus = ErrorType.AUTHORIZATION_ERROR.getStatus().value();
        String expectedContentType = "application/json;charset=UTF-8";

        new DefaultAuthenticationEntryPoint().commence(null, response, null);

        Mockito.verify(response, Mockito.times(1)).setStatus(expectedStatus);
        Mockito.verify(response, Mockito.times(1)).setContentType(expectedContentType);
    }
}
