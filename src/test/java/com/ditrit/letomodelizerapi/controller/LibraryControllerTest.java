package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.library.LibraryDTO;
import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.LibraryService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: LibraryController")
class LibraryControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    LibraryService libraryService;

    @InjectMocks
    LibraryController controller;

    @Test
    @DisplayName("Test findAll: should return valid response with permission to get all libraries.")
    void testFindAllWithPermissionAllLibraries() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(this.libraryService.findAll(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<LibraryDTO>> response = this.controller.findAll(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test findAll: should return valid response with permission to get specific libraries.")
    void testFindAllWithPermissionSpecificLibraries() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(this.libraryService.findAll(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<LibraryDTO>> response = this.controller.findAll(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getLibraryById: should return valid response.")
    void testGetLibraryById() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(this.libraryService.findById(Mockito.any())).thenReturn(new Library());
        final ResponseEntity<LibraryDTO> response = this.controller.getLibraryById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test createLibrary: should return valid response.")
    void testCreateLibrary() throws JsonProcessingException {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        User user = new User();
        user.setLogin("login");
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(this.libraryService.create(Mockito.any(), Mockito.any())).thenReturn(new Library());
        final ResponseEntity<LibraryDTO> response = this.controller.createLibrary(request, new LibraryRecord("url", "role"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test validateLibrary: should return valid response.")
    void testValidateLibrary() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        User user = new User();
        user.setLogin("login");
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.libraryService).validateLibrary(Mockito.any());
        final ResponseEntity<Object> response = this.controller.validateLibrary(request, "url");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test updateLibrary: should return valid response.")
    void testUpdateLibrary() throws JsonProcessingException {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.libraryService).update(Mockito.any(), Mockito.any());
        final ResponseEntity<Object> response = this.controller.updateLibrary(request, UUID.randomUUID(), "url");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test deleteLibrary: should return valid response.")
    void testDeleteLibrary() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.libraryService).delete(Mockito.any());
        final ResponseEntity<Object> response = this.controller.deleteLibrary(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test findAllTemplates: should return valid response with permission to get all templates.")
    void testFindAllTemplatesWithPermissionAllLibraries() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(this.libraryService.findAllTemplates(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<LibraryTemplate>> response = this.controller.findAllTemplates(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test findAllTemplates: should return valid response with permission to get specific templates.")
    void testFindAllTemplatesWithPermissionSpecificLibraries() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(this.libraryService.findAllTemplates(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<LibraryTemplate>> response = this.controller.findAllTemplates(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getIcon: should return valid response.")
    void testGetIcon() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        LibraryController controller = new LibraryController(userService, userPermissionService, libraryService, "1");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpResponse<byte[]> icon = Mockito.mock(HttpResponse.class);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(libraryService.getIcon(Mockito.any()))
                .thenReturn(icon);

        ResponseEntity<byte[]> response = controller.getLibraryIcon(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getLibraryTemplates: should return valid response.")
    void testGetLibraryTemplates() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(this.libraryService.findAllTemplates(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<LibraryTemplate>> response = this.controller.getLibraryTemplates(request, UUID.randomUUID(), new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getTemplatesById: should return valid response.")
    void testGetTemplatesById() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(this.libraryService.getTemplateById(Mockito.any())).thenReturn(new LibraryTemplate());
        final ResponseEntity<LibraryTemplate> response = this.controller.getTemplatesById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getTemplateIcon: should return valid response.")
    void testGetTemplateIcon() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        LibraryController controller = new LibraryController(userService, userPermissionService, libraryService, "1");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpResponse<byte[]> icon = Mockito.mock(HttpResponse.class);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(libraryService.getTemplateIcon(Mockito.any()))
                .thenReturn(icon);
        Mockito
                .when(libraryService.getTemplateById(Mockito.any()))
                .thenReturn(new LibraryTemplate());

        ResponseEntity<byte[]> response = controller.getTemplateIcon(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getTemplateSchema: should return valid response.")
    void testGetTemplateSchema() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        LibraryController controller = new LibraryController(userService, userPermissionService, libraryService, "1");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpResponse<byte[]> icon = Mockito.mock(HttpResponse.class);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(libraryService.getTemplateSchema(Mockito.any(), Mockito.any()))
                .thenReturn(icon);
        Mockito
                .when(libraryService.getTemplateById(Mockito.any()))
                .thenReturn(new LibraryTemplate());
        Mockito
                .when(libraryService.getFileName(Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
                .thenReturn("test");

        ResponseEntity<byte[]> response = controller.getTemplateSchema(request, UUID.randomUUID(), 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getTemplateFile: should return valid response.")
    void testGetTemplateFile() {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        LibraryController controller = new LibraryController(userService, userPermissionService, libraryService, "1");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpResponse<byte[]> file = Mockito.mock(HttpResponse.class);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(libraryService.getTemplateFile(Mockito.any(), Mockito.any()))
                .thenReturn(file);
        LibraryTemplate template = new LibraryTemplate();
        template.setFiles(List.of("test"));
        Mockito
                .when(libraryService.getTemplateById(Mockito.any()))
                .thenReturn(template);

        ResponseEntity<byte[]> response = controller.getTemplateFile(request, UUID.randomUUID(), 0L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
