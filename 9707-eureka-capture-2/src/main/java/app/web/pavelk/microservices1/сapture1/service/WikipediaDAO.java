package app.web.pavelk.microservices1.сapture1.service;

import app.web.pavelk.microservices1.сapture1.model.Article;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WikipediaDAO {
    private final Map<String, Article> mapDb = new ConcurrentHashMap<>();//Параллельный
    String[] name = {"article1.txt"};

    public Article get(String url) {
        return mapDb.get(url);
    }

    public void save(Article article) {
        if (article == null || article.getUrl() == null)
            return;
        mapDb.put(article.getUrl(), article);
    }

    public void saveFile() {
        createFile();
        AtomicInteger index = new AtomicInteger(0);
        try {
            Files.write(Paths.get(name[0]),
                    mapDb.values().stream().map(m -> {
                        index.getAndIncrement();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(m.getUrl()).append("\n");
                        stringBuilder.append(m.getText()).append("\n");
                        stringBuilder.append(m.getCaption()).append("\n--").append(index.get()).append("--\n");
                        return stringBuilder;
                    }).collect(Collectors.toList()),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFile() {
        Arrays.stream(name).forEach(f -> {
            try {
                Files.write(
                        Paths.get(f),
                        "new \n".getBytes(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
