package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import com.ditrit.letomodelizerapi.persistence.repository.AIConfigurationRepository;
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
@DisplayName("Test class: AIConfigurationImpl")
class AIConfigurationServiceImplTest {

    @Mock
    AIConfigurationRepository aiConfigurationRepository;

    @InjectMocks
    AIConfigurationServiceImpl service;

    @Test
    @DisplayName("Test findAll: should return wanted configuration")
    void testFindAll() {
        AIConfiguration configuration = new AIConfiguration();
        configuration.setId(UUID.randomUUID());
        configuration.setKey("key");
        configuration.setValue("value");

        Pageable pageable = PageRequest.of(0, 10);
        Page<AIConfiguration> page = new PageImpl<>(List.of(configuration), pageable, 1);

        Mockito.when(aiConfigurationRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(page);

        var result = service.findAll(Map.of(), Pageable.ofSize(10));
        assertEquals(configuration, result.getContent().getFirst());
    }

    @Test
    @DisplayName("Test findById: should return wanted configuration")
    void testFindById() {
        AIConfiguration configuration = new AIConfiguration();
        configuration.setId(UUID.randomUUID());
        configuration.setKey("key");
        configuration.setValue("value");

        Mockito.when(aiConfigurationRepository.findById(Mockito.any())).thenReturn(Optional.of(configuration));

        assertEquals(configuration, service.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Test findById: should throw an exception on invalid id")
    void testFindByIdError() {
        AIConfiguration configuration = new AIConfiguration();
        configuration.setId(UUID.randomUUID());
        configuration.setKey("key");
        configuration.setValue("value");

        Mockito.when(aiConfigurationRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ApiException exception = null;
        var uuid = UUID.randomUUID();
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
    @DisplayName("Test create: should create configuration")
    void testCreate() {
        AIConfiguration configuration = new AIConfiguration();
        configuration.setId(UUID.randomUUID());
        configuration.setKey("key");
        configuration.setValue("value");

        Mockito.when(aiConfigurationRepository.existsByHandlerAndKey(Mockito.any(), Mockito.any()))
                .thenReturn(false);
        Mockito.when(aiConfigurationRepository.save(Mockito.any())).thenReturn(configuration);

        var result = service.create(new AIConfigurationRecord("handler", "key", "value"));
        assertEquals(configuration, result);
    }

    @Test
    @DisplayName("Test create: should throw an exception on already exists entity")
    void testCreateError() {
        Mockito.when(aiConfigurationRepository.existsByHandlerAndKey(Mockito.any(), Mockito.any()))
                .thenReturn(true);
        ApiException exception = null;

        try {
            service.create(new AIConfigurationRecord("handler", "key", "value"));
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
    @DisplayName("Test update: should update configuration")
    void testUpdate() {
        AIConfiguration configuration = new AIConfiguration();
        configuration.setId(UUID.randomUUID());
        configuration.setKey("key");
        configuration.setValue("value");

        Mockito.when(aiConfigurationRepository.findById(Mockito.any())).thenReturn(Optional.of(configuration));
        Mockito.when(aiConfigurationRepository.save(Mockito.any())).thenReturn(configuration);

        var result = service.update(UUID.randomUUID(), new AIConfigurationRecord("handler", "key",
                "value"));
        assertEquals(configuration, result);
    }

    @Test
    @DisplayName("Test delete: should delete configuration")
    void testDelete() {
        AIConfiguration configuration = new AIConfiguration();
        configuration.setId(UUID.randomUUID());
        configuration.setKey("key");
        configuration.setValue("value");

        Mockito.when(aiConfigurationRepository.findById(Mockito.any())).thenReturn(Optional.of(configuration));
        Mockito.doNothing().when(aiConfigurationRepository).delete(Mockito.any());

        service.delete(UUID.randomUUID());

        Mockito.verify(aiConfigurationRepository, Mockito.times(1)).delete(Mockito.any());
    }
}
