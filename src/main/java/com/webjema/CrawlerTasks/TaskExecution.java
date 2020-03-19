package com.webjema.CrawlerTasks;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.webjema.models.DocumentDataModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class TaskExecution {
    protected static final Logger LOGGER = LogManager.getLogger(TaskExecution.class.getName());
    protected DynamoDBMapper ddbm;

    public TaskExecutionResult Execute(TaskData taskData, DynamoDBMapper ddbm) throws IOException {
        LOGGER.warn("Default task execution was called for " + taskData.getDonorName());
        TaskExecutionResult result = new TaskExecutionResult();
        return result;
    }

    protected String getUncompressedContent(InputStream stream, String encoding) throws IOException {
        if (encoding.equalsIgnoreCase("gzip")) {
            return ungzipContent(stream);
        }
        LOGGER.warn("Unrecognized encoding '" + encoding + "'! Return content as is.");
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    protected void saveDocumentsLinks(TaskData taskData, List<String> links) {
        for (String link : links) {
            DocumentDataModel docData = new DocumentDataModel();
            docData.setDonorName(taskData.getDonorName());
            docData.setDocumentLink(link);
            this.ddbm.save(docData);
        }

        /*
        List<UpdateItemSpec> itemsToPut = new ArrayList<>();
        links.forEach(link -> itemsToPut.add(new UpdateItemSpec()
                            .withPrimaryKey("DonorName", taskData.getDonorName(), "DocumentLink", link)
                            .withAttributeUpdate(new AttributeUpdate("FoundTime").put(Instant.now().toString()))
                            )
        );
        Table table = this.ddbm.getTable("DonorsDocuments");
        try {
            itemsToPut.forEach(item -> {
                UpdateItemOutcome outcome = table.updateItem(item);
                System.out.println("UpdateItem succeeded: " + outcome.getUpdateItemResult().toString());
            });
        }
        catch (Exception e) {
            LOGGER.error("Error during saving documents links: " + e.getMessage());
            LOGGER.error(e);
            for (StackTraceElement el : e.getStackTrace()) {
                System.out.println(el.toString());
            }
        }

         */
    }
/*
    private void saveItemsToDB(List<Item> itemsToPut) {
        TableWriteItems writeItems = new TableWriteItems("DonorsDocuments").withItemsToPut(itemsToPut);
        BatchWriteItemOutcome outcome = this.ddb.batchWriteItem(writeItems);
        LOGGER.info("Save links to DDB outcome is " + outcome.getBatchWriteItemResult().toString());
        if (outcome.getBatchWriteItemResult().getUnprocessedItems().size() > 0)
        {
            LOGGER.error("Save links to DDB has unprocessed items! Size " + outcome.getBatchWriteItemResult().getUnprocessedItems().size());
        }
    }

 */

    private String ungzipContent(InputStream stream) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(stream);
        StringBuffer  szBuffer = new StringBuffer ();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = gzip.read(buffer, 0, 1024)) != -1) {
            szBuffer.append (new String (buffer, 0, length));
        }
        return szBuffer.toString();
    }

}
