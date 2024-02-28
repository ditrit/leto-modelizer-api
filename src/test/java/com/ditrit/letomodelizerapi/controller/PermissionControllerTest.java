package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.PermissionService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: PermissionController")
class PermissionControllerTest extends MockHelper {

    @Mock
    private UserService userService;

    @Mock
    private UserPermissionService userPermissionService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController controller;

    @Test
    @DisplayName("Test findAll: should return valid response")
    void testFindAll() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(new User());
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(permissionService.findAll(Mockito.any(), Mockito.any())).thenReturn(Page.empty());

        Response response = this.controller.findAll(request, mockUriInfo(), new QueryFilter());
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }
}
