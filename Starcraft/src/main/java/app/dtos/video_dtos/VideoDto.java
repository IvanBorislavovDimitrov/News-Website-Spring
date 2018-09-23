package app.dtos.video_dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoDto {

    private int id;

    private String name;

    private int likes;

    private int dislikes;

    private Date date;

    private List<VideoCommentDto> videoComments;

    public VideoDto() {
        this.videoComments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<VideoCommentDto> getVideoComments() {
        return videoComments;
    }

    public void setVideoComments(List<VideoCommentDto> videoComments) {
        this.videoComments = videoComments;
    }
}
