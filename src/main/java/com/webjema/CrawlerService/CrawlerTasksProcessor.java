package com.webjema.CrawlerService;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

import java.util.List;

public class CrawlerTasksProcessor {

    private String queueName;

    public CrawlerTasksProcessor(String queueName) {
        this.queueName = queueName;
    }

    public void Poller() {
        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        final String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

        SetQueueAttributesRequest set_attrs_request = new SetQueueAttributesRequest()
                .withQueueUrl(queueUrl)
                .addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");
        sqs.setQueueAttributes(set_attrs_request);

        final ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(20);

        while (true) {
            List<Message> messages = this.getMessages(sqs, receiveRequest);
            if (messages.size() > 0) {
                messages.forEach(message -> processMessage(sqs, message));
            }
        }
    }

    private List<Message> getMessages(AmazonSQS sqs, ReceiveMessageRequest receiveRequest) {
        final ReceiveMessageResult result = sqs.receiveMessage(receiveRequest);
        return result.getMessages();
    }

    private void processMessage(AmazonSQS sqs, Message message) {
        System.out.println("Message received: " + message.getBody());
        sqs.deleteMessage(queueName, message.getReceiptHandle());
    }
}
