package com.webjema.CrawlerTasks.TaskExecutors;

import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecution;
import com.webjema.CrawlerTasks.TaskExecutionResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupTaskExecution extends TaskExecution {

    @Override
    public TaskExecutionResult Execute(TaskData taskData) {
        LOGGER.info("[JSOUP] Execution of task " + taskData.getDonorName());
        TaskExecutionResult result = new TaskExecutionResult();

        Document doc = new Document("");
        Connection connection = Jsoup.connect(taskData.getStartUrl());
        connection.timeout(5000);
        connection.headers(taskData.getHeaders());
        try {
            doc = connection.get();
        } catch (IOException e) {
            LOGGER.warn("Can't load URL " + taskData.getStartUrl());
            e.printStackTrace();
            result.statusCode = 404;
            return result;
        }

        LOGGER.info("Response content = " + doc.outerHtml());
        return result;
    }

}
