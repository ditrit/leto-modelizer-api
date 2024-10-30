package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AISecretRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import com.ditrit.letomodelizerapi.persistence.model.AISecret;
import com.ditrit.letomodelizerapi.persistence.repository.AIConfigurationRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AISecretRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: AISecretImpl")
class AISecretServiceImplTest {

    @Mock
    AISecretRepository aiSecretRepository;

    @Mock
    AIConfigurationRepository aiConfigurationRepository;

    @InjectMocks
    AISecretServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AISecretServiceImpl(aiSecretRepository, aiConfigurationRepository, "password1",
                "password2");
    }

    @Test
    @DisplayName("Test findAll: should return wanted secret without it's value")
    void testFindAll() {
        AISecret secret = new AISecret();
        secret.setId(UUID.randomUUID());
        secret.setKey("key");
        secret.setValue("value".getBytes());

        AISecret expectedSecret = new AISecret();
        expectedSecret.setId(secret.getId());
        expectedSecret.setKey("key");

        Pageable pageable = PageRequest.of(0, 10);
        Page<AISecret> page = new PageImpl<>(List.of(secret), pageable, 1);

        Mockito.when(aiSecretRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(page);

        var result = service.findAll(Map.of(), Pageable.ofSize(10));
        assertEquals(expectedSecret, result.getContent().getFirst());
    }

    @Test
    @DisplayName("Test findById: should return wanted secret without it's value")
    void testFindById() {
        AISecret secret = new AISecret();
        secret.setId(UUID.randomUUID());
        secret.setKey("key");
        secret.setValue("value".getBytes());

        AISecret expectedSecret = new AISecret();
        expectedSecret.setId(secret.getId());
        expectedSecret.setKey("key");

        Mockito.when(aiSecretRepository.findById(Mockito.any())).thenReturn(Optional.of(secret));

        assertEquals(expectedSecret, service.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Test create: should create secret")
    void testCreate() {
        AISecret secret = new AISecret();
        secret.setId(UUID.randomUUID());
        secret.setKey("key");
        secret.setValue("value".getBytes());

        AISecret expectedSecret = new AISecret();
        expectedSecret.setId(secret.getId());
        expectedSecret.setKey("key");

        Mockito.when(aiSecretRepository.existsByKey(Mockito.any())).thenReturn(false);
        Mockito.when(aiSecretRepository.save(Mockito.any())).thenReturn(secret);

        var result = service.create(new AISecretRecord("key", "value"));
        assertEquals(expectedSecret, result);
    }

    @Test
    @DisplayName("Test create: should throw an exception on already exists entity")
    void testCreateError() {
        Mockito.when(aiSecretRepository.existsByKey(Mockito.any())).thenReturn(true);
        ApiException exception = null;

        try {
            service.create(new AISecretRecord("key", "value"));
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals("key", exception.getError().getField());
        assertEquals("key", exception.getError().getValue());
    }

    @Test
    @DisplayName("Test update: should update secret")
    void testUpdate() {
        AISecret secret = new AISecret();
        secret.setId(UUID.randomUUID());
        secret.setKey("key");
        secret.setValue("value".getBytes());

        AISecret expectedSecret = new AISecret();
        expectedSecret.setId(secret.getId());
        expectedSecret.setKey("key");

        Mockito.when(aiSecretRepository.findById(Mockito.any())).thenReturn(Optional.of(secret));
        Mockito.when(aiSecretRepository.save(Mockito.any())).thenReturn(secret);

        var result = service.update(UUID.randomUUID(), new AISecretRecord("key", "value"));
        assertEquals(expectedSecret, result);
    }

    @Test
    @DisplayName("Test delete: should delete secret")
    void testDelete() {
        AISecret secret = new AISecret();
        secret.setId(UUID.randomUUID());
        secret.setKey("key");
        secret.setValue("value".getBytes());
        Mockito.when(aiSecretRepository.findById(Mockito.any())).thenReturn(Optional.of(secret));
        Mockito.doNothing().when(aiSecretRepository).delete(Mockito.any());

        service.delete(UUID.randomUUID());

        Mockito.verify(aiSecretRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test generateConfiguration: should generate configuration")
    void testGenerateConfiguration() {
        var configuration1 = new AIConfiguration();
        configuration1.setKey("key1");
        configuration1.setValue("value1");
        var configuration2 = new AIConfiguration();
        configuration2.setHandler("test");
        configuration2.setKey("key2");
        configuration2.setValue("{{secrets.secret1}}");
        var configuration3 = new AIConfiguration();
        configuration3.setKey("key3");
        configuration3.setValue(null);
        var secret1 = new AISecret();
        secret1.setKey("secret1");
        secret1.setValue(service.encrypt("password1", "value2"));

        Mockito.when(aiConfigurationRepository.findAll()).thenReturn(List.of(configuration1, configuration2, configuration3));
        Mockito.when(aiSecretRepository.findAll()).thenReturn(List.of(secret1));

        var result = service.generateConfiguration();

        assertEquals("{\"key1\":\"value1\",\"test.key2\":\"value2\",\"key3\":\"\"}", service.decrypt("password2", result));
    }
}
