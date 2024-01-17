package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.user.UserRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: UserServiceImpl")
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl service;

    static MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
    static MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);

    @Test
    @DisplayName("Test save: should save new user on unknown login.")
    void testSaveUnknownUser() {
        User expectedUser = new User();
        UserRecord record = new UserRecord("email", "login", "name", "picture");
        Mockito
            .when(userRepository.save(Mockito.any()))
            .thenReturn(expectedUser);
        Mockito
            .when(userRepository.findByLogin(Mockito.any()))
            .thenReturn(Optional.empty());

        assertEquals(expectedUser, service.save(record));
    }

    @Test
    @DisplayName("Test save: should update user with existing login.")
    void testSaveUser() {
        User expectedUser = new User();
        UserRecord record = new UserRecord("email", "login", "name", "picture");
        Mockito
            .when(userRepository.save(Mockito.any()))
            .thenReturn(expectedUser);
        Mockito
            .when(userRepository.findByLogin(Mockito.any()))
            .thenReturn(Optional.of(expectedUser));

        assertEquals(expectedUser, service.save(record));
    }

    @Test
    @DisplayName("Test getFromSession: should throw exception on unknown login.")
    void testGetFromSessionUnknownUser() {
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito
            .when(session.getAttribute(Mockito.any()))
            .thenReturn("login_value");
        Mockito
            .when(userRepository.findByLogin(Mockito.any()))
            .thenReturn(Optional.empty());

        ApiException exception = null;

        try {
            service.getFromSession(session);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.AUTHORIZATION_ERROR.getStatus(), exception.getStatus());
        assertEquals("login", exception.getError().getField());
        assertEquals("login_value", exception.getError().getValue());
    }

    @Test
    @DisplayName("Test getFromSession: should get user with existing login.")
    void testGetFromSession() {
        User expectedUser = new User();
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito
            .when(session.getAttribute(Mockito.any()))
            .thenReturn("login_value");
        Mockito
            .when(userRepository.findByLogin(Mockito.any()))
            .thenReturn(Optional.of(expectedUser));

        assertEquals(expectedUser, service.getFromSession(session));
    }

    @Test
    @DisplayName("Test getPicture: Should throw a valid exception on error.")
    void testGetPictureThrowException() throws IOException, InterruptedException {
        User user = new User();
        user.setPicture("picture_value");
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenThrow(new IOException("test"));
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        ApiException exception = null;

        try {
            service.getPicture(user);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.INTERNAL_ERROR.getStatus(), exception.getStatus());
        assertEquals("picture", exception.getError().getField());
        assertEquals("picture_value", exception.getError().getValue());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test getPicture: Should return picture on success.")
    void testGetPicture() throws IOException, InterruptedException {
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);
        User user = new User();
        user.setPicture("picture_value");
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        assertEquals(expectedResponse, service.getPicture(user));
    }
}
