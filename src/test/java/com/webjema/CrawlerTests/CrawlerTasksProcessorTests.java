package com.webjema.CrawlerTests;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.webjema.CrawlerService.CrawlerTasksProcessor;
import com.webjema.CrawlerService.TaskExecutionFactory;
import com.webjema.CrawlerTasks.TaskData;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

public class CrawlerTasksProcessorTests {
    /*
    @Test
    public void processMessageJsonParsingTestPositive() {
        System.out.println("Testing processMessageJsonParsingTestPositive");

        AmazonSQS sqs = mock(AmazonSQS.class);
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(this.getTestMessage());

        JsoupTaskExecution taskExecution = mock(JsoupTaskExecution.class);

        TaskExecutionFactory taskExecutionFactory = mock(TaskExecutionFactory.class);
        when(taskExecutionFactory.getTaskExecution(any(TaskData.class))).thenReturn(taskExecution);

        CrawlerTasksProcessor processor = new CrawlerTasksProcessor("queue-name", 1, taskExecutionFactory);
        processor.processMessage(sqs, message);

        ArgumentCaptor<TaskData> taskDataArgumentCaptor = ArgumentCaptor.forClass(TaskData.class);
        verify(taskExecution, times(1)).Execute(taskDataArgumentCaptor.capture());

        Assert.assertEquals(taskDataArgumentCaptor.getValue().getDonorName(), "remax");
        Assert.assertEquals(taskDataArgumentCaptor.getValue().getHeaders().get("authority"), "www.remax.lu");

        // steps check
        System.out.println("Total steps found: " + taskDataArgumentCaptor.getValue().getTaskSteps().size());
        taskDataArgumentCaptor.getValue().getTaskSteps().forEach(step -> System.out.println("Step validation: " + step.getTaskStartValidation()));
    }
    */

    @Test
    public void processMessageJsonParsingTestPositive() throws JsonProcessingException {
        System.out.println("Testing processMessageJsonParsingTestPositive");

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(this.getTestMessage());
        TaskExecutionFactory taskExecutionFactory = mock(TaskExecutionFactory.class);

        DynamoDBMapper ddbm = mock(DynamoDBMapper.class);

        CrawlerTasksProcessor processor = new CrawlerTasksProcessor("queue-name", 1, taskExecutionFactory, ddbm);
        TaskData task = processor.getTaskData(message);

        Assert.assertEquals(task.getDonorName(), "remax.lu");
        Assert.assertEquals(task.getHeaders().get("authority"), "www.remax.lu");

        // steps check
        System.out.println("Total steps found: " + task.getTaskSteps().size());
        task.getTaskSteps().forEach(step -> System.out.println("Step validation: " + step.getTaskStartValidation()));
    }

    private String getTestMessage() {
        return this.readFileContent("src/test/resources/sqs_message_remax.json");
    }

    private static String readFileContent(String filePath)
    {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }
}
