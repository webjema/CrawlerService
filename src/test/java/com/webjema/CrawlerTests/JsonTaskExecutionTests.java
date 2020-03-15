package com.webjema.CrawlerTests;

import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.webjema.CrawlerService.CrawlerTasksProcessor;
import com.webjema.CrawlerService.TaskExecutionFactory;
import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecutionResult;
import com.webjema.CrawlerTasks.TaskExecutors.JsonTaskExecution;
import com.webjema.CrawlerTasks.TaskExecutors.JsoupTaskExecution;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.*;

public class JsonTaskExecutionTests {
    @Test
    public void executeInJsonTaskExecutionTestPositive() throws IOException {
        System.out.println("Testing JsoupTaskExecution");

        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(this.getTestMessage());
        TaskExecutionFactory taskExecutionFactory = mock(TaskExecutionFactory.class);

        CrawlerTasksProcessor processor = new CrawlerTasksProcessor("queue-name", 1, taskExecutionFactory);
        TaskData task = processor.getTaskData(message);

        JsonTaskExecution jsonTaskExecution = new JsonTaskExecution();
        TaskExecutionResult result = jsonTaskExecution.Execute(task);
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
