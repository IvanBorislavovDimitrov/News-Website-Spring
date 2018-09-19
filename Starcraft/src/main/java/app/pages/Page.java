package app.pages;

import app.dtos.article_dtos.ArticleDto;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private int number;
    private List<ArticleDto> articles;

    public Page() {
        this.articles = new ArrayList<>();
    }

    public List<ArticleDto> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleDto> articles) {
        this.articles = articles;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
