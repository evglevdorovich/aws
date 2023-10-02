package com.example.warehouse.utils;

import com.example.warehouse.exception.NotParsableMessageToObjectException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    public T extractFromMessageWithAttribute(Message message, String attributeName,  Class<T> clazz) {
        try {
            var messageJsonNode = mapper.readTree(message.body()).get("Message");
            var attributeValue = message.messageAttributes().get(attributeName).stringValue();

            if (messageJsonNode.isObject()) {
                ObjectNode objectNode = (ObjectNode) messageJsonNode;
                objectNode.put(attributeName, attributeValue);
            }

            return mapper.readValue(messageJsonNode.asText(), clazz);
        } catch (JsonProcessingException e) {
            throw new NotParsableMessageToObjectException(e.getMessage());
        }
    }
}
