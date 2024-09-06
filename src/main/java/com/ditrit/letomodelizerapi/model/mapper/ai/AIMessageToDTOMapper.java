package com.ditrit.letomodelizerapi.model.mapper.ai;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AIMessageDTO;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;

import java.util.Base64;
import java.util.function.Function;

public class AIMessageToDTOMapper implements Function<AIMessage, AIMessageDTO> {
    @Override
    public AIMessageDTO apply(final AIMessage aiMessage) {
        AIMessageDTO aiMessageDTO = new BeanMapper<>(AIMessageDTO.class, "message").apply(aiMessage);

        aiMessageDTO.setMessage(Base64.getEncoder().encodeToString(aiMessage.getMessage()));

        return aiMessageDTO;
    }
}
