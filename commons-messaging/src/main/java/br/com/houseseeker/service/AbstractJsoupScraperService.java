package br.com.houseseeker.service;

import jakarta.validation.constraints.NotBlank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractJsoupScraperService<T> {

    public final T scrap(@NotBlank String content) {
        return scrapDocument(Jsoup.parse(content));
    }

    protected abstract T scrapDocument(Document document);


}
