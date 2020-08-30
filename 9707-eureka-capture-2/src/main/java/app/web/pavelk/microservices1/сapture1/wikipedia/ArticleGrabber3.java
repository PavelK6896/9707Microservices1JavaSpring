package app.web.pavelk.microservices1.сapture1.wikipedia;

import app.web.pavelk.microservices1.сapture1.exception.GrabberException;
import app.web.pavelk.microservices1.сapture1.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class ArticleGrabber3 {

    public Article grab(String url) {
        if (url == null || url.isEmpty())
            return null;

        log.debug("Получение статьи: {}", url);

        String protocol;
        String host;
        try {
            URL u = new URL(url);
            protocol = u.getProtocol();
            host = u.getHost();

            if (!host.contains("wikipedia"))
                throw new GrabberException("Это не адрес Википедии");
        } catch (MalformedURLException e) {
            log.error("Неверный url: {}", url);
            return null;
        } catch (GrabberException gex) {
            log.error(gex.getMessage());
            return null;
        }

        try {
            Document doc = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/70.0.3538.77 Safari/537.36").get();


            if (doc.select("body.mediawiki").isEmpty())
                return null;

            Article article = new Article();
            article.setCaption(doc.select("#firstHeading").text());
            article.setUrl(url);
            article.setText(doc.select("#bodyContent").text());

            //парсим все сылки с одной странице в сет
            for (Element e : doc.select("#bodyContent a[href*=/wiki/]")) {
                Article a = new Article();
                String href = e.attr("href");
                if (href.startsWith("/wiki/"))
                    a.setUrl(protocol + "://" + host + href);
                else
                    a.setUrl(e.attr("href"));
                a.setCaption(e.attr("title"));
                if (a.getCaption().isEmpty())
                    continue;
                article.getRelations().add(a);
            }

            log.info(" сылок найдено = " + (article.getRelations().size()));

            return article;

        } catch (IOException iex) {
            log.error("Ошибка загрузки страницы", iex);
            return null;
        }

    }

}
