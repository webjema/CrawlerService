package com.webjema.CrawlerTasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;

public class TaskExecution {
    protected static final Logger LOGGER = LogManager.getLogger(TaskExecution.class.getName());

    public TaskExecutionResult Execute(TaskData taskData) throws MalformedURLException {
        LOGGER.warn("Default task execution was called for " + taskData.getDonorName());
        TaskExecutionResult result = new TaskExecutionResult();
        return result;
    }
}
