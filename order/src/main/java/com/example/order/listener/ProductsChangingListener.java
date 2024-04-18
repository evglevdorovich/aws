package com.example.order.listener;

import com.example.order.config.ReplaceNotifierQueueConfigProperties;
import com.example.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductsChangingListener {
    private final SqsClient amazonSQS;
    private final ProductService productService;
    private final ReplaceNotifierQueueConfigProperties replaceNotifierQueueConfigProperties;

    @Scheduled(cron= "*/30 * * * * *")
    public void replaceProductOnChanges() {
        String queueUrl = getQueueUrl();
        List<Message> messages = receiveMessages(queueUrl);

        if (!messages.isEmpty()) {
            productService.replaceProducts();
            amazonSQS.purgeQueue(PurgeQueueRequest.builder().queueUrl(queueUrl).build());
        }
    }

    private List<Message> receiveMessages(String queueUrl) {
        var receiveMessageRequest = ReceiveMessageRequest
                .builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(replaceNotifierQueueConfigProperties.waitTimeSeconds())
                .maxNumberOfMessages(replaceNotifierQueueConfigProperties.maxNumberOfMessages())
                .build();

        return amazonSQS.receiveMessage(receiveMessageRequest).messages();
    }

    private String getQueueUrl() {
        var getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(replaceNotifierQueueConfigProperties.name())
                .build();

        return amazonSQS.getQueueUrl(getQueueUrlRequest).queueUrl();
    }

}
