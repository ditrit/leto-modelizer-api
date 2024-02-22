package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import com.ditrit.letomodelizerapi.service.UserCsrfTokenService;
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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: CsrfController")
class CsrfControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserPermissionService userPermissionService;

    @Mock
    private UserCsrfTokenService userCsrfTokenService;

    @InjectMocks
    private CsrfController controller;

    @Test
    @DisplayName("Test home: should return valid response")
    void testHome() {
        CsrfToken expectedToken = new DefaultCsrfToken("test", "test", "test");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getAttribute(Mockito.any())).thenReturn(expectedToken);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(new User());
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(userCsrfTokenService.findByLogin(Mockito.any())).thenReturn(new UserCsrfToken());

        Response response = controller.getCsrfToken(request);
        assertNotNull(response.getEntity());
    }
}
