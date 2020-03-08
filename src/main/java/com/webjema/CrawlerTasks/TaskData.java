package com.webjema.CrawlerTasks;
import lombok.Data;
import java.util.List;

@Data
public class TaskData {
    private String donorName;
    private String startUrl;
    private List<TaskStep> taskSteps;
}
