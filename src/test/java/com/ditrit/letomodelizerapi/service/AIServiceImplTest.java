package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("AIServiceImpl Unit Tests")
class AIServiceImplTest {

    AIServiceImpl newInstance() {
        return new AIServiceImpl("http://localhost:8585/");
    }

    private static void mockHttpCall(MockedStatic<HttpClient> clientStatic, int expectedStatus, String expectedBody) throws IOException, InterruptedException {
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);

        HttpClient client = Mockito.mock(HttpClient.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(expectedStatus);
        if (expectedBody != null){
            Mockito.when(expectedResponse.body()).thenReturn(expectedBody);
        }
        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);
    }
    
    @Test
    @DisplayName("Test createFile: should return valid response when a valid request is done")
    void testCreateFile() throws IOException, InterruptedException {

        String expectedBody = "[{\"name\": \"deployment.yaml\", \"content\": \"apiVersion: apps/v1\\nkind: Deployment\"}]";
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic, HttpStatus.OK.value(), expectedBody);

        AIServiceImpl service = newInstance();

        AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "I want a sample of kubernetes code");
        assertEquals(expectedBody, service.createFile(aiCreateFileRecord));

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test createFile: should return a 530 due to an error in the body")
    void testCreateFileWrongBody() throws IOException, InterruptedException {

        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,530, null);

        AIServiceImpl service = newInstance();

        ApiException exception = null;

        try {
            AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "coucou");
            service.createFile(aiCreateFileRecord);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.AI_GENERATION_ERROR.getStatus(), exception.getStatus());
        assertEquals(ErrorType.AI_GENERATION_ERROR.getMessage(), exception.getMessage());

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test createFile: should return a 206 (Wrong value) due to an error in the url")
    void testCreateFileWrongUrl() throws IOException, InterruptedException {

        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,400, null);

        AIServiceImpl service = newInstance();

        ApiException exception = null;

        try {
            AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "coucou");
            service.createFile(aiCreateFileRecord);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.WRONG_VALUE.getMessage(), exception.getMessage());

        Mockito.reset();
        clientStatic.close();
    }

}

