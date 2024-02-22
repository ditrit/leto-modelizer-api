package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import com.ditrit.letomodelizerapi.persistence.repository.UserCsrfTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: UserCsrfTokenServiceImpl")
class UserCsrfTokenServiceImplTest {

    @Mock
    UserCsrfTokenRepository userCsrfTokenRepository;

    @InjectMocks
    UserCsrfTokenServiceImpl service;

    @Test
    @DisplayName("Test findByLogin: should retrieve user.")
    void testFindByLogin() {
        UserCsrfToken expected = new UserCsrfToken();
        Mockito.when(userCsrfTokenRepository.findByLogin(Mockito.any())).thenReturn(Optional.of(expected));
        assertEquals(expected, service.findByLogin("test"));
    }

    @Test
    @DisplayName("Test findByLogin: should throw exception on unknown user.")
    void testFindByLoginUnknown() {
        Mockito.when(userCsrfTokenRepository.findByLogin(Mockito.any())).thenReturn(Optional.empty());
        ApiException exception = null;

        try {
            service.findByLogin("test");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("token", exception.getError().getField());
    }
}
