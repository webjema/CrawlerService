package com.webjema.CrawlerDonors;

import com.webjema.CrawlerTasks.TaskData;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class RemaxLuProcessor extends DonorProcessor {

    @Override
    public DonorLinksResult processDocumentForLinks(TaskData taskData, Document document) {
        DonorLinksResult results = new DonorLinksResult();
        Elements documentsLinks = document.select(".LinkImage");
        results.documentsLinks = new ArrayList<>();
        documentsLinks.forEach(el -> results.documentsLinks.add(
                this.linksNormalizing(taskData, el.attr("href").substring(0, el.attr("href").indexOf('?')))
        ));

        Elements pagesLinks = document.select(".ajax-page-link");
        results.pagesLinks = new ArrayList<>();
        pagesLinks.forEach(el -> results.pagesLinks.add(this.linksNormalizing(taskData, el.attr("href"))));

        return results;
    }
}
