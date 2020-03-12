package com.webjema.CrawlerTests;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.webjema.CrawlerService.CrawlerTasksProcessor;
import com.webjema.CrawlerService.TaskExecutionFactory;
import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecution;
import com.webjema.CrawlerTasks.TaskExecutors.JsoupTaskExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.Any;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

public class CrawlerTasksProcessorTests {
    @Test
    public void processMessageJsonParsingTestPositive() {
        System.out.println("Testing processMessageJsonParsingTestPositive");
        //System.out.println("JSON message: " + this.getTestMessage());

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
        System.out.println("taskDataArgumentCaptor.getValue().getDonorName() = " + taskDataArgumentCaptor.getValue().getDonorName());
        System.out.println("taskDataArgumentCaptor.getValue().getHeaders() 'authority' = " + taskDataArgumentCaptor.getValue().getHeaders().get("authority"));
    }

    private String getTestMessage() {
        return this.readFileContent("src/test/resources/sqs_message_1.json");
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
