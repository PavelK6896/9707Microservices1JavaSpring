package app.web.pavelk.microservices1.сapture1;

import app.web.pavelk.microservices1.сapture1.exception.GrabberException;
import app.web.pavelk.microservices1.сapture1.wikipedia.WikipediaGrabber1;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Capture1 {


    public static void main(String[] args) {
        WikipediaGrabber1 wikipediaGrabber1 = new WikipediaGrabber1();

        try {//корневая страница
            wikipediaGrabber1.start("https://ru.wikipedia.org/wiki/%D0%9D%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5_%D0%A4%D1%80%D0%B0%D0%BD%D1%86%D0%B8%D0%B8");
        } catch (GrabberException e) {
            log.error(e.getMessage());
            System.exit(1);
        }

        //сохранит в файл
        wikipediaGrabber1.getWikipediaDAO().saveFile();

        System.exit(0);
    }


}
