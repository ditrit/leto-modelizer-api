package com.ditrit.letomodelizerapi.model.mapper.ai;

import com.ditrit.letomodelizerapi.model.ai.AIMessageDTO;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: AIMessageToDTOMapper")
class AIMessageToDTOMapperTest {

    @Test
    @DisplayName("Test apply: should map to dto.")
    void testApply() {
        final AIMessageToDTOMapper mapper = new AIMessageToDTOMapper();

        AIMessage message = new AIMessage();
        message.setMessage("test".getBytes());

        AIMessageDTO dto = mapper.apply(message);
        assertNotNull(dto);
        assertEquals("test", new String(Base64.getDecoder().decode(dto.getMessage())));
    }
}
