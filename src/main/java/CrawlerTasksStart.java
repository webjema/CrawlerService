import com.webjema.CrawlerService.CrawlerTasksProcessor;
import com.webjema.CrawlerService.TaskExecutionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Integer.parseInt;

public class CrawlerTasksStart {

    public static void main(String[] args) throws InterruptedException {
        final Logger LOGGER = LogManager.getLogger(CrawlerTasksStart.class.getName());
        LOGGER.info("CrawlerTasks Started!");
        int tasks_quantity = parseInt(System.getenv("PARALLEL_TASKS_QNT"));
        String webDriverURL = System.getenv("WEB_DRIVER_URL");

        CrawlerTasksProcessor processor = new CrawlerTasksProcessor(args[0], tasks_quantity, new TaskExecutionFactory(webDriverURL));
        processor.Poller();
    }

}
