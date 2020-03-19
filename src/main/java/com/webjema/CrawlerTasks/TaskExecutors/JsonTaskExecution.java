package com.webjema.CrawlerTasks.TaskExecutors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webjema.CrawlerDonors.DonorLinksResult;
import com.webjema.CrawlerDonors.DonorProcessor;
import com.webjema.CrawlerDonors.DonorsProcessorsFactory;
import com.webjema.CrawlerTasks.TaskData;
import com.webjema.CrawlerTasks.TaskExecution;
import com.webjema.CrawlerTasks.TaskExecutionResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonTaskExecution extends TaskExecution {

    @Override
    public TaskExecutionResult Execute(TaskData taskData, DynamoDBMapper ddbm) throws IOException {
        this.ddbm = ddbm;
        LOGGER.info("[JSON] Execution of task " + taskData.getDonorName() + " with DDB = " + this.ddbm);
        TaskExecutionResult result = new TaskExecutionResult();

        URL url = new URL(taskData.getBaseUrl() + taskData.getStartUri());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        taskData.getHeaders().forEach((k,v)->connection.setRequestProperty(k, v));

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();
        String encoding = connection.getContentEncoding();
        LOGGER.info("[JSON] load URL status is " + status + " | and encoding " + encoding);

        String content = this.getUncompressedContent(connection.getInputStream(), encoding);
        connection.disconnect();
        //LOGGER.info("Response content = " + content);

        JsonNode jsonNode = new ObjectMapper().readTree(content);
        Document document = Jsoup.parse(jsonNode.get("llContentContainerHtml").asText());

        //LOGGER.info("Document content = " + document.outerHtml());
        DonorProcessor donorProcessor = DonorsProcessorsFactory.createProcessor(taskData);
        DonorLinksResult linkResults = donorProcessor.processDocumentForLinks(taskData, document);

        linkResults.documentsLinks.forEach(l -> System.out.println("Document link: " + l));
        linkResults.pagesLinks.forEach(l -> System.out.println("Page link: " + l));

        this.saveDocumentsLinks(taskData, linkResults.documentsLinks);
        return result;
    }

}
