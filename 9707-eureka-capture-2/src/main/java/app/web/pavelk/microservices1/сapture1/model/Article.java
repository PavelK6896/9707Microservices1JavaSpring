package app.web.pavelk.microservices1.сapture1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Article {//Статья

    @EqualsAndHashCode.Include //Включать
    private String url;

    private String text;
    private String caption;
    private Set<Article> relations = new HashSet<>();//отношения
}
