package app.pages;

import app.dtos.video_dtos.VideoDto;

import java.util.ArrayList;
import java.util.List;

public class PageVideos {

    private int number;
    private List<VideoDto> videos;

    public PageVideos() {
        this.videos = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<VideoDto> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDto> videos) {
        this.videos = videos;
    }
}
