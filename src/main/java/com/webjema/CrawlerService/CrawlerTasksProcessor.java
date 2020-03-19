package com.webjema.CrawlerService;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecution;
import com.webjema.CrawlerTasks.TaskExecutionResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrawlerTasksProcessor {
    private String queueName;
    private int tasksQuantity;
    private TaskExecutionFactory taskExecutionFactory;
    private DynamoDBMapper ddbm;

    private static final Logger LOGGER = LogManager.getLogger(CrawlerTasksProcessor.class.getName());

    public CrawlerTasksProcessor(String queueName, int tasksQuantity, TaskExecutionFactory taskExecutionFactory, DynamoDBMapper ddbm) {
        this.queueName = queueName;
        this.tasksQuantity = tasksQuantity;
        this.taskExecutionFactory = taskExecutionFactory;
        this.ddbm = ddbm;
    }

    public void Poller() throws InterruptedException {
        final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1)
                .withCredentials(new EnvironmentVariableCredentialsProvider()) // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
                .withClientConfiguration(new ClientConfiguration()
                        .withMaxConnections(10))
                .build();

        final String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl(); // not URL! Just name!

        SetQueueAttributesRequest set_attrs_request = new SetQueueAttributesRequest()
                .withQueueUrl(queueUrl)
                .addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");
        sqs.setQueueAttributes(set_attrs_request);

        final ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withWaitTimeSeconds(20);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.tasksQuantity);

        while (true) {
            List<Message> messages = this.getMessages(sqs, receiveRequest);
            if (messages.size() > 0) {
                LOGGER.info("Some messages received! Count: " + messages.size());
                messages.forEach(message -> {
                    executor.submit(() -> processMessage(sqs, message)); // https://www.baeldung.com/thread-pool-java-and-guava
                });
            }
            // wait for tasks execution
            while(executor.getQueue().size() > 1) {
                TimeUnit.SECONDS.sleep(1);
            }
            TimeUnit.SECONDS.sleep(30);
        }
    }

    private List<Message> getMessages(AmazonSQS sqs, ReceiveMessageRequest receiveRequest) {
        final ReceiveMessageResult result = sqs.receiveMessage(receiveRequest);
        return result.getMessages();
    }

    @VisibleForTesting
    public void processMessage(AmazonSQS sqs, Message message) {
        LOGGER.info("Message received: " + message.getBody());
        TaskData task;
        try {
            task = this.getTaskData(message);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error during parsing JSON task:" + message.getBody());
            e.printStackTrace();
            return;
        }
        TaskExecution taskExecution = this.taskExecutionFactory.getTaskExecution(task);
        try {
            final TaskExecutionResult result = taskExecution.Execute(task, this.ddbm);
        } catch (MalformedURLException e) {
            LOGGER.error("Error during executing task " + task.getDonorName());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("Error during executing task " + task.getDonorName());
            e.printStackTrace();
        }
        sqs.deleteMessage(queueName, message.getReceiptHandle());
    }

    @VisibleForTesting
    public TaskData getTaskData(Message message) throws JsonProcessingException {
        return new ObjectMapper().readValue(message.getBody(), TaskData.class);
    }
}
