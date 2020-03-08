package com.webjema.CrawlerTasks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TaskExecution {

    private WebDriver webdriver;
    private TaskData taskData;
    private String webDriverURL;

    public TaskExecution(TaskData taskData, String webDriverURL)
    {
        this.taskData = taskData;
        this.webDriverURL = webDriverURL;
    }

    public TaskExecutionResult Execute() throws MalformedURLException {
        System.out.println("Execution of task " + taskData.getDonorName());
        TaskExecutionResult result = new TaskExecutionResult();
        DesiredCapabilities capability = DesiredCapabilities.chrome();
        webdriver = new RemoteWebDriver(new URL(webDriverURL), capability);

        System.out.println("Open URL " + taskData.getStartUrl());
        webdriver.get(taskData.getStartUrl());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // end
        webdriver.quit();
        return result;
    }

}
