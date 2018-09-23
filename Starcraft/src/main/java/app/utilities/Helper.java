package app.utilities;

import app.dtos.article_dtos.ArticleDto;
import app.models.Video;
import app.pages.Page;
import app.pages.PageVideos;

import java.util.List;

public class Helper {

    public static void addArticlesToPages(List<ArticleDto> news, Page[] pages, int count, int maxArticles) {
        for (int i = 0; count < news.size(); i++) {
            pages[i] = new Page();
            pages[i].setNumber(i + 1);
            for (int j = 0; j < maxArticles && count < news.size(); j++) {
                pages[i].getArticles().add(news.get(count++));
            }
        }
    }

    public static void addVideosToPages(List<Video> videos, PageVideos[] pages, int count, int maxArticles) {
        for (int i = 0; count < videos.size(); i++) {
            pages[i] = new PageVideos();
            pages[i].setNumber(i + 1);
            for (int j = 0; j < maxArticles && count < videos.size(); j++) {
                pages[i].getVideos().add(videos.get(count++));
            }
        }
    }
}
