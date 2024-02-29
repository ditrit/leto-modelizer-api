package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.repository.LibraryRepository;
import com.ditrit.letomodelizerapi.persistence.repository.LibraryTemplateRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserLibraryTemplateViewRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserLibraryViewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: LibraryServiceImpl")
class LibraryServiceImplTest {

    @Mock
    LibraryRepository libraryRepository;

    @Mock
    LibraryTemplateRepository libraryTemplateRepository;

    @Mock
    AccessControlService accessControlService;

    @Mock
    UserLibraryViewRepository userLibraryViewRepository;

    @Mock
    UserLibraryTemplateViewRepository userLibraryTemplateViewRepository;

    @Mock
    PermissionService permissionService;

    String loadJson(final String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode json = mapper.readTree(this.getClass().getClassLoader().getResourceAsStream(path));

        return json.toString();
    }

    LibraryServiceImpl newInstance(String url) {
        return new LibraryServiceImpl(
                libraryRepository,
                libraryTemplateRepository,
                accessControlService,
                permissionService,
                userLibraryViewRepository,
                userLibraryTemplateViewRepository,
                url
        );
    }

    void mockHttp(String json, MockedStatic<HttpRequest> requestStatic, MockedStatic<HttpClient> clientStatic) throws IOException, InterruptedException {
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(HttpStatus.OK.value());
        Mockito.when(expectedResponse.body()).thenReturn(json);
        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);
    }

    HttpResponse<byte[]> mockHttpFile(MockedStatic<HttpRequest> requestStatic, MockedStatic<HttpClient> clientStatic) throws IOException, InterruptedException {
        HttpResponse<byte[]> expectedResponse = Mockito.mock(HttpResponse.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(HttpStatus.OK.value());
        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        HttpResponse.BodyHandler<byte[]> captor = HttpResponse.BodyHandlers.ofByteArray();

        Mockito.when(client.send(Mockito.any(), Mockito.any(captor.getClass()))).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        return expectedResponse;
    }

    @Test
    @DisplayName("Test downloadLibrary: should return valid response")
    void testDownloadLibrary() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(HttpStatus.OK.value());
        Mockito.when(expectedResponse.body()).thenReturn("ok");
        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        assertEquals("ok", service.downloadLibrary("test"));

        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test downloadLibrary: should throw an error on NOT_FOUND error")
    void testDownloadLibraryThrowNotFound() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(HttpStatus.NOT_FOUND.value());
        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        ApiException exception = null;

        try {
            newInstance("http://localhost:8080/test/").downloadLibrary("test");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals("url", exception.getError().getField());
        assertEquals("test", exception.getError().getValue());
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test downloadLibrary: should throw an error on ioexception")
    void testDownloadLibraryThrowError() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
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
            newInstance("http://localhost:8080/test/").downloadLibrary("test");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals("url", exception.getError().getField());
        assertEquals("test", exception.getError().getValue());
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test downloadLibraryFile: should return valid response")
    void testDownloadLibraryFile() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(HttpStatus.OK.value());
        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        assertEquals(expectedResponse, service.downloadLibraryFile("test", "test.svg"));

        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test downloadLibraryFile: should throw an error on NOT_FOUND error")
    void testDownloadLibraryFileThrowNotFound() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);
        HttpRequest.Builder requestBuilder = Mockito.mock(HttpRequest.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(HttpStatus.NOT_FOUND.value());
        Mockito.when(requestBuilder.uri(Mockito.any())).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.GET()).thenReturn(requestBuilder);
        Mockito.when(requestBuilder.build()).thenReturn(request);

        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        requestStatic.when(HttpRequest::newBuilder).thenReturn(requestBuilder);
        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);

        ApiException exception = null;

        try {
            newInstance("http://localhost:8080/test/").downloadLibraryFile("test", "test.svg");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.LIBRARY_FILE_DOWNLOAD_ERROR.getStatus(), exception.getStatus());
        assertEquals("file", exception.getError().getField());
        assertEquals("test.svg", exception.getError().getValue());
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test downloadLibraryFile: should throw an error on ioexception")
    void testDownloadLibraryFileThrowError() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
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
            newInstance("http://localhost:8080/test/").downloadLibraryFile("test", "test.svg");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.LIBRARY_FILE_DOWNLOAD_ERROR.getStatus(), exception.getStatus());
        assertEquals("file", exception.getError().getField());
        assertEquals("test.svg", exception.getError().getValue());
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test save: should create new library")
    void testSave() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());

        Mockito.when(libraryRepository.save(Mockito.any())).thenReturn(expectedLibrary);
        Mockito.when(libraryTemplateRepository.saveAll(Mockito.any())).thenReturn(List.of());
        mockHttp(loadJson("libraries/valid/simple/index.json"), requestStatic, clientStatic);
        LibraryServiceImpl service = newInstance("test");

        assertEquals(expectedLibrary, service.save("url", null));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test validateLibrary: should validate library")
    void testValidateLibrary() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());

        mockHttp(loadJson("libraries/valid/simple/index.json"), requestStatic, clientStatic);
        LibraryServiceImpl service = newInstance("test");

        ApiException exception = null;
        try {
            service.validateLibrary("test");
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test save: should update library")
    void testSaveUpdate() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());

        Mockito.when(libraryRepository.save(Mockito.any())).thenReturn(expectedLibrary);
        Mockito.when(libraryTemplateRepository.saveAll(Mockito.any())).thenReturn(List.of());
        mockHttp(loadJson("libraries/valid/simple/index.json"), requestStatic, clientStatic);
        LibraryServiceImpl service = newInstance("test");

        assertEquals(expectedLibrary, service.save("url", null));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test create: should throw an error on unauthorized library url")
    void testCreateWithUnauthorizedUrl() throws JsonProcessingException {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        ApiException exception = null;

        try {
            service.create(new LibraryRecord("bad", null), "login");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.UNAUTHORIZED_LIBRARY_URL.getStatus(), exception.getStatus());
        assertEquals(ErrorType.UNAUTHORIZED_LIBRARY_URL.getMessage(), exception.getMessage());
        assertEquals("url", exception.getError().getField());
        assertEquals("bad", exception.getError().getValue());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test validateLibrary: should throw an error on unauthorized library url")
    void testValidateLibraryWithUnauthorizedUrl() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        ApiException exception = null;

        try {
            service.validateLibrary("bad");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.UNAUTHORIZED_LIBRARY_URL.getStatus(), exception.getStatus());
        assertEquals(ErrorType.UNAUTHORIZED_LIBRARY_URL.getMessage(), exception.getMessage());
        assertEquals("url", exception.getError().getField());
        assertEquals("bad", exception.getError().getValue());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test create: should throw an error on already exists library url")
    void testCreateWithAlreadyExistsUrl() throws JsonProcessingException {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Mockito.when(libraryRepository.existsByUrl(Mockito.any())).thenReturn(true);

        ApiException exception = null;

        try {
            service.create(new LibraryRecord("http://localhost:8080/test/index.json", null), "login");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals("url", exception.getError().getField());
        assertEquals("http://localhost:8080/test/", exception.getError().getValue());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test create: should create role and library")
    void testCreate() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        AccessControl role = new AccessControl();
        role.setId(UUID.randomUUID());

        String validJson = loadJson("libraries/valid/simple/index.json");
        mockHttp(validJson, requestStatic, clientStatic);

        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());

        Mockito.when(libraryRepository.save(Mockito.any())).thenReturn(expectedLibrary);
        Mockito.when(libraryRepository.existsByUrl(Mockito.any())).thenReturn(false);
        Mockito.when(accessControlService.create(Mockito.any(), Mockito.any())).thenReturn(role);
        Mockito.doNothing().when(accessControlService).associateUser(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(permissionService).createLibraryPermissions(Mockito.any(), Mockito.any());

        assertEquals(expectedLibrary, service.create(new LibraryRecord("http://localhost:8080/test/index.json", "test"), "login"));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test update: should throw an error on unauthorized library url")
    void testUpdateWithUnauthorizedUrl() throws JsonProcessingException {
        Library library = new Library();
        library.setId(UUID.randomUUID());
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(library));
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        ApiException exception = null;

        try {
            service.update(UUID.randomUUID(), "bad");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.UNAUTHORIZED_LIBRARY_URL.getStatus(), exception.getStatus());
        assertEquals(ErrorType.UNAUTHORIZED_LIBRARY_URL.getMessage(), exception.getMessage());
        assertEquals("url", exception.getError().getField());
        assertEquals("bad", exception.getError().getValue());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test create: should throw an error on already exists library url")
    void testUpdateWithAlreadyExistsUrl() throws JsonProcessingException {
        Library library = new Library();
        library.setId(UUID.randomUUID());
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(library));
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Mockito.when(libraryRepository.existsByUrlAndIdIsNot(Mockito.any(), Mockito.any())).thenReturn(true);

        ApiException exception = null;

        try {
            service.update(UUID.randomUUID(), "http://localhost:8080/test/index.json");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals("url", exception.getError().getField());
        assertEquals("http://localhost:8080/test/", exception.getError().getValue());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test update: should update library")
    void testUpdate() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        Library library = new Library();
        library.setId(UUID.randomUUID());
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(library));
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        AccessControl role = new AccessControl();
        role.setId(UUID.randomUUID());

        String validJson = loadJson("libraries/valid/simple/index.json");
        mockHttp(validJson, requestStatic, clientStatic);

        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());

        Mockito.when(libraryRepository.save(Mockito.any())).thenReturn(expectedLibrary);
        Mockito.when(libraryRepository.existsByUrlAndIdIsNot(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.doNothing().when(libraryTemplateRepository).deleteByLibraryId(Mockito.any());

        ApiException exception = null;

        try {
            service.update(UUID.randomUUID(), "http://localhost:8080/test/index.json");
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test validate: should not throw exception on valid json")
    void testValidate() throws IOException {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        String validJson = loadJson("libraries/valid/simple/index.json");
        ApiException exception = null;

        try {
            service.validate(validJson);
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
        Mockito.reset();
    }

    @Test
    @DisplayName("Test validate: should throw exception on invalid json")
    void testValidateError() throws IOException {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        String invalidJson = "{}";
        ApiException exception = null;

        try {
            service.validate(invalidJson);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_LIBRARY_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.WRONG_LIBRARY_VALUE.getMessage(), exception.getMessage());
        Mockito.reset();
    }

    @Test
    @DisplayName("Test findById: should return library")
    void testFindById() throws IOException {
        UUID uuid = UUID.randomUUID();
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Library expectedLibrary = new Library();
        expectedLibrary.setId(uuid);
        Mockito.when(libraryRepository.findById(uuid)).thenReturn(Optional.of(expectedLibrary));

        assertEquals(expectedLibrary, service.findById(uuid));
    }

    @Test
    @DisplayName("Test findById: should throw exception")
    void testFindByIdError() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ApiException exception = null;
        UUID uuid = UUID.randomUUID();

        try {
            service.findById(uuid);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals("id", exception.getError().getField());
        assertEquals(uuid.toString(), exception.getError().getValue());
    }

    @Test
    @DisplayName("Test delete: should delete library")
    void testDelete() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(expectedLibrary));
        Mockito.doNothing().when(libraryRepository).delete(Mockito.any());

        service.delete(UUID.randomUUID());

        Mockito.verify(libraryRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test findAll: should return libraries")
    void testFindAll() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        Mockito.when(libraryRepository.findAll(Mockito.any(Specification.class), Mockito.any())).thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(Map.of(), PageRequest.of(1, 1)));
    }

    @Test
    @DisplayName("Test findAll: should return libraries by userId")
    void testFindAllByUser() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        Mockito.when(userLibraryViewRepository.findAll(Mockito.any(Specification.class), Mockito.any())).thenReturn(Page.empty());
        User user = new User();
        user.setId(UUID.randomUUID());
        assertEquals(Page.empty(), service.findAll(user, Map.of(), PageRequest.of(1, 1)));
    }

    @Test
    @DisplayName("Test findAllTemplates: should return templates")
    void testFindAllTemplates() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        Mockito.when(libraryTemplateRepository.findAll(Mockito.any(Specification.class), Mockito.any())).thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAllTemplates(Map.of(), PageRequest.of(1, 1)));
    }

    @Test
    @DisplayName("Test findAllTemplates: should return templates by userId")
    void testFindAllTemplatesByUser() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        Mockito.when(userLibraryTemplateViewRepository.findAllByUserId(Mockito.any(), Mockito.any(Specification.class), Mockito.any())).thenReturn(Page.empty());
        User user = new User();
        user.setId(UUID.randomUUID());
        assertEquals(Page.empty(), service.findAllTemplates(user, Map.of(), PageRequest.of(1, 1)));
    }

    @Test
    @DisplayName("Test getTemplateById: should return library template")
    void testGetTemplateById() throws IOException {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate expectedLibraryTemplate = new LibraryTemplate();
        expectedLibraryTemplate.setId(UUID.randomUUID());
        Mockito.when(libraryTemplateRepository.findById(Mockito.any())).thenReturn(Optional.of(expectedLibraryTemplate));

        assertEquals(expectedLibraryTemplate, service.getTemplateById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Test getTemplateById: should throw exception")
    void testGetTemplateByIdError() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Mockito.when(libraryTemplateRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ApiException exception = null;
        UUID uuid = UUID.randomUUID();

        try {
            service.getTemplateById(uuid);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals("id", exception.getError().getField());
        assertEquals(uuid.toString(), exception.getError().getValue());
    }

    @Test
    @DisplayName("Test getIcon: should return icon")
    void testGetIcon() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Library library = new Library();
        library.setId(UUID.randomUUID());
        library.setIcon("icon.svg");
        UUID uuid = UUID.randomUUID();
        Mockito.when(libraryRepository.findById(uuid)).thenReturn(Optional.of(library));
        HttpResponse<byte[]> expectedResponse = mockHttpFile(requestStatic, clientStatic);

        assertEquals(expectedResponse, service.getIcon(uuid));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test getIcon: should throw exception")
    void testGetIconError() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        Library library = new Library();
        UUID uuid = UUID.randomUUID();
        Mockito.when(libraryRepository.findById(uuid)).thenReturn(Optional.of(library));
        ApiException exception = null;

        try {
            service.getIcon(uuid);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.EMPTY_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.EMPTY_VALUE.getMessage(), exception.getMessage());
        assertEquals("icon", exception.getError().getField());
    }

    @Test
    @DisplayName("Test getTemplateIcon: should return icon")
    void testGetTemplateIcon() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate libraryTemplate = new LibraryTemplate();
        libraryTemplate.setId(UUID.randomUUID());
        libraryTemplate.setIcon("icon.svg");
        Library library = new Library();
        library.setId(UUID.randomUUID());
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(library));
        HttpResponse<byte[]> expectedResponse = mockHttpFile(requestStatic, clientStatic);

        assertEquals(expectedResponse, service.getTemplateIcon(libraryTemplate));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test getTemplateIcon: should throw exception")
    void testGetTemplateIconError() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate libraryTemplate = new LibraryTemplate();
        ApiException exception = null;

        try {
            service.getTemplateIcon(libraryTemplate);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.EMPTY_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.EMPTY_VALUE.getMessage(), exception.getMessage());
        assertEquals("icon", exception.getError().getField());
    }

    @Test
    @DisplayName("Test getTemplateSchema: should return schema")
    void testGetTemplateSchema() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");

        LibraryTemplate libraryTemplate = new LibraryTemplate();
        libraryTemplate.setId(UUID.randomUUID());
        libraryTemplate.setIcon("icon.svg");
        libraryTemplate.setSchemas(List.of("test.svg"));

        Library library = new Library();
        library.setId(UUID.randomUUID());

        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(library));
        HttpResponse<byte[]> expectedResponse = mockHttpFile(requestStatic, clientStatic);

        assertEquals(expectedResponse, service.getTemplateSchema(libraryTemplate, 0L));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test getTemplateSchema: should throw exception")
    void testGetTemplateSchemaError() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate libraryTemplate = new LibraryTemplate();
        libraryTemplate.setSchemas(List.of("test.svg"));
        ApiException exception = null;

        try {
            service.getTemplateSchema(libraryTemplate, 1L);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.WRONG_VALUE.getMessage(), exception.getMessage());
        assertEquals("index", exception.getError().getField());
        assertEquals("1", exception.getError().getValue());
    }

    @Test
    @DisplayName("Test getTemplateFile: should return file")
    void testGetTemplateFile() throws IOException, InterruptedException {
        MockedStatic<HttpRequest> requestStatic = Mockito.mockStatic(HttpRequest.class);
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate libraryTemplate = new LibraryTemplate();
        libraryTemplate.setId(UUID.randomUUID());
        libraryTemplate.setIcon("icon.svg");
        libraryTemplate.setFiles(List.of("test.svg"));
        Library library = new Library();
        library.setId(UUID.randomUUID());
        Mockito.when(libraryRepository.findById(Mockito.any())).thenReturn(Optional.of(library));
        HttpResponse<byte[]> expectedResponse = mockHttpFile(requestStatic, clientStatic);

        assertEquals(expectedResponse, service.getTemplateFile(libraryTemplate, 0L));
        Mockito.reset();
        requestStatic.close();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test getTemplateFile: should throw exception")
    void testGetTemplateFileError() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate libraryTemplate = new LibraryTemplate();
        libraryTemplate.setFiles(List.of("test.svg"));
        ApiException exception = null;

        try {
            service.getTemplateFile(libraryTemplate, 1L);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.WRONG_VALUE.getMessage(), exception.getMessage());
        assertEquals("index", exception.getError().getField());
        assertEquals("1", exception.getError().getValue());
    }

    @Test
    @DisplayName("Test getFileName: should return filename")
    void testGetFileName() {
        LibraryServiceImpl service = newInstance("http://localhost:8080/test/");
        LibraryTemplate template = new LibraryTemplate();
        template.setFiles(List.of("/other1/test1/file1.xml", "/test2/file2.xml", "file3.xml"));
        template.setSchemas(List.of("/other3/test3/schema1.xml", "/test4/schema2.xml", "schema3.xml"));

        assertEquals("file1.xml", service.getFileName(false, template, 0L));
        assertEquals("file2.xml", service.getFileName(false, template, 1L));
        assertEquals("file3.xml", service.getFileName(false, template, 2L));
        assertEquals("schema1.xml", service.getFileName(true, template, 0L));
        assertEquals("schema2.xml", service.getFileName(true, template, 1L));
        assertEquals("schema3.xml", service.getFileName(true, template, 2L));
    }
}
