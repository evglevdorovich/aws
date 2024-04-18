package com.example.payment.utils;

import com.example.payment.exception.NotParsableMessageToObjectException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
@RequiredArgsConstructor
public class ObjectMessageExtractor<T> {
    private final ObjectMapper mapper;
    public T extractFromMessage(Message message, Class<T> clazz) {
        try {
            var messageJson = mapper.readTree(message.body()).get("Message");
            return mapper.readValue(messageJson.asText(), clazz);
        } catch (JsonProcessingException e) {
            throw new NotParsableMessageToObjectException(e.getMessage());
        }
    }
}
