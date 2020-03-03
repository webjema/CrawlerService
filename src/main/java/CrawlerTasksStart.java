import com.webjema.CrawlerService.CrawlerTasksProcessor;

public class CrawlerTasksStart {

    public static void main(String[] args) throws InterruptedException {
        CrawlerTasksProcessor processor = new CrawlerTasksProcessor(args[0]);
        processor.Poller();
    }

}
