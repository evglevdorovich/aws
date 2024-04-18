package com.example.order.utils;

import com.example.order.exception.NotParsableMessageToObjectException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
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

    public T extractFromMessageWithAttribute(Message message, String attributeName, Class<T> clazz) {
        try {
            var messageJsonNode = mapper.readTree(message.body()).get("Message");
            var attributeValue = receiveAttributeValue(message, attributeName);
            var object = mapper.readValue(messageJsonNode.asText(), clazz);

            setAttributeField(attributeName, attributeValue, object);

            return object;
        } catch (JsonProcessingException e) {
            throw new NotParsableMessageToObjectException(e.getMessage());
        }
    }

    private void setAttributeField(String attributeName, String attributeValue, T object) {
        var attributeField = ReflectionUtils.findField(object.getClass(), attributeName);
        attributeField.setAccessible(true);
        ReflectionUtils.setField(attributeField, object, attributeValue);
    }

    private String receiveAttributeValue(Message message, String attributeName) throws JsonProcessingException {
        return mapper.readTree(message.body())
                .get("MessageAttributes")
                .get(attributeName)
                .get("Value")
                .asText();
    }
}
