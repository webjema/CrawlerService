package com.webjema.CrawlerDonors;

import com.webjema.CrawlerTasks.TaskData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class DonorsProcessorsFactory {
    protected static final Logger LOGGER = LogManager.getLogger(DonorsProcessorsFactory.class.getName());

    public static DonorProcessor createProcessor(TaskData taskData) {
        try {
            Class clazz = Class.forName("com.webjema.CrawlerDonors." + taskData.getDonorProcessor());
            return (DonorProcessor)clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        LOGGER.error("Can't create donor processor with name " + taskData.getDonorProcessor());
        return null;
    }
}
