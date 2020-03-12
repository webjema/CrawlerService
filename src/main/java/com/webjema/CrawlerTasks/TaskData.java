package com.webjema.CrawlerTasks;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TaskData {
    private String donorName;
    private ExecutorTypes executor;
    private String startUrl;
    private Map<String,String> headers = new HashMap<>();
    private List<TaskStep> taskSteps;
}
