import com.webjema.CrawlerService.CrawlerTasksProcessor;

import static java.lang.Integer.parseInt;

public class CrawlerTasksStart {

    public static void main(String[] args) throws InterruptedException {
        int tasks_quantity = parseInt(System.getenv("PARALLEL_TASKS_QNT"));
        String webDriverURL = System.getenv("WEB_DRIVER_URL");
        CrawlerTasksProcessor processor = new CrawlerTasksProcessor(args[0], tasks_quantity, webDriverURL);
        processor.Poller();
    }

}
