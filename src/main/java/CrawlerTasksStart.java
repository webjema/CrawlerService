import com.webjema.CrawlerService.CrawlerTasksProcessor;

public class CrawlerTasks {

    public static void main(String[] args) {
        CrawlerTasksProcessor processor = new CrawlerTasksProcessor(args[0]);
        processor.Poller();
    }

}
