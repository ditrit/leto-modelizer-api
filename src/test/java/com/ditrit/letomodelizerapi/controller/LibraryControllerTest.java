package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.LibraryService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

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
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(this.libraryService.findAll(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final Response response = this.controller.findAll(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test findAll: should return valid response with permission to get specific libraries.")
    void testFindAllWithPermissionSpecificLibraries() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(this.libraryService.findAll(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final Response response = this.controller.findAll(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getLibraryById: should return valid response.")
    void testGetLibraryById() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.when(this.libraryService.findById(Mockito.any())).thenReturn(new Library());
        final Response response = this.controller.getLibraryById(request, 1l);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
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
        final Response response = this.controller.createLibrary(request, new LibraryRecord("url", "role"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateLibrary: should return valid response.")
    void testUpdateLibrary() throws JsonProcessingException {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.libraryService).update(Mockito.any(), Mockito.any());
        final Response response = this.controller.updateLibrary(request, 1L, "url");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test deleteLibrary: should return valid response.")
    void testDeleteLibrary() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkLibraryPermission(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.libraryService).delete(Mockito.any());
        final Response response = this.controller.deleteLibrary(request, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test findAllTemplates: should return valid response with permission to get all templates.")
    void testFindAllTemplatesWithPermissionAllLibraries() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(this.libraryService.findAllTemplates(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final Response response = this.controller.findAllTemplates(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test findAllTemplates: should return valid response with permission to get specific templates.")
    void testFindAllTemplatesWithPermissionSpecificLibraries() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(this.libraryService.findAllTemplates(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final Response response = this.controller.findAllTemplates(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }
}
