package app.web.pavelk.microservices1.сapture1.wikipedia;

import app.web.pavelk.microservices1.сapture1.model.Article;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ArticleWorker2 implements Callable<Article> {
    private WikipediaGrabber1 wikipediaGrabber1;
    private String url;

    public ArticleWorker2(WikipediaGrabber1 wikipediaGrabber1, String url) {
        this.wikipediaGrabber1 = wikipediaGrabber1;
        this.url = url;
    }

    @Override
    public Article call() throws Exception {
         Thread.sleep(ThreadLocalRandom.current().nextInt(0, wikipediaGrabber1.getMaxDelay() + 1) * 1000);

        ArticleGrabber3 articleGrabber3 = new ArticleGrabber3();
        Article article = wikipediaGrabber1.getWikipediaDAO().get(this.url);//dto

        if (article != null)
            return article;

        article = articleGrabber3.grab(this.url);//получение статьи по вызову метода гет в футуре
        wikipediaGrabber1.getWikipediaDAO().save(article);
        return article;
    }


}
