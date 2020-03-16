package com.webjema.CrawlerTasks.TaskExecutors;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecution;
import com.webjema.CrawlerTasks.TaskExecutionResult;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WebdriverTaskExecution extends TaskExecution {

    private WebDriver webdriver;
    private String webDriverURL;

    public WebdriverTaskExecution(String webDriverURL)
    {
        this.webDriverURL = webDriverURL;
    }

    @Override
    public TaskExecutionResult Execute(TaskData taskData, DynamoDB ddb) throws MalformedURLException {
        LOGGER.info("[WEBDRIVER] Execution of task " + taskData.getDonorName());
        TaskExecutionResult result = new TaskExecutionResult();
        DesiredCapabilities capability = DesiredCapabilities.chrome();
        webdriver = new RemoteWebDriver(new URL(webDriverURL), capability);
        final String url = taskData.getBaseUrl() + taskData.getStartUri();
        LOGGER.info("Open URL " + url);
        webdriver.get(url);
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
