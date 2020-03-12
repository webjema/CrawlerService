package com.webjema.CrawlerService;

import com.webjema.CrawlerTasks.ExecutorTypes;
import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecution;
import com.webjema.CrawlerTasks.TaskExecutors.JsoupTaskExecution;
import com.webjema.CrawlerTasks.TaskExecutors.WebdriverTaskExecution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskExecutionFactory {
    private static final Logger LOGGER = LogManager.getLogger(TaskExecutionFactory.class.getName());
    private String webDriverURL;

    public TaskExecutionFactory (String webDriverURL) {
        this.webDriverURL = webDriverURL;
    }

    public TaskExecution getTaskExecution(TaskData task) {
        TaskExecution taskExecution;
        if (task.getExecutor() == ExecutorTypes.WEBDRIVER) {
            taskExecution = new WebdriverTaskExecution(webDriverURL);
        } else if (task.getExecutor() == ExecutorTypes.JSOUP) {
            taskExecution = new JsoupTaskExecution();
        } else {
            taskExecution = new TaskExecution();
            LOGGER.error("Executor type is not supported. Found " + task.getExecutor());
        }
        return taskExecution;
    }
}
