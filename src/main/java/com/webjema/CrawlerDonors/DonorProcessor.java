package com.webjema.CrawlerDonors;

import com.webjema.CrawlerTasks.TaskData;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.net.URISyntaxException;

public class DonorProcessor {
    protected static final Logger LOGGER = LogManager.getLogger(DonorProcessor.class.getName());

    public DonorLinksResult processDocumentForLinks(TaskData taskData, Document document) {
        LOGGER.error("Default donor processor has been called! It's not correct.");
        return null;
    }

    protected String linksNormalizing(TaskData taskData, String link) {
        //URL url = new URL()
        URIBuilder builder = null;
        try {
            builder = new URIBuilder(taskData.getBaseUrl());
            builder.setPath(link);
            return builder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        LOGGER.error("Can't build URL");
        return null;
    }
}
