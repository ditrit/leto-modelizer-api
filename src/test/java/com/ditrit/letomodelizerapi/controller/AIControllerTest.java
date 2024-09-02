package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIService;
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
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: AIController")
class AIControllerTest extends MockHelper {

    @Mock
    UserService userService;
    @Mock
    AIService service;

    @InjectMocks
    AIController controller;

    @Test
    @DisplayName("Test generateFiles: should return valid response.")
    void testGenerateFiles() throws InterruptedException {
        User user = new User();
        user.setLogin("login");
        AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "I want a sample of kubernetes code");

        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);

        Mockito.when(this.service.createFile(aiCreateFileRecord)).thenReturn("OK");
        final Response response = this.controller.generateFiles(request, aiCreateFileRecord);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

}
