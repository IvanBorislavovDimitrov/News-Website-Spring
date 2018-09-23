package app.pages;

import app.models.Video;

import java.util.ArrayList;
import java.util.List;

public class PageVideos {

    private int number;
    private List<Video> videos;

    public PageVideos() {
        this.videos = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
