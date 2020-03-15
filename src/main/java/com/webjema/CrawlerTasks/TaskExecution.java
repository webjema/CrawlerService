package com.webjema.CrawlerTasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.zip.GZIPInputStream;

public class TaskExecution {
    protected static final Logger LOGGER = LogManager.getLogger(TaskExecution.class.getName());

    public TaskExecutionResult Execute(TaskData taskData) throws IOException {
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
