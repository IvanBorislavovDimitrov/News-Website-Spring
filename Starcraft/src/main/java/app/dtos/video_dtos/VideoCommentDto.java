package app.dtos.video_dtos;

import java.util.Date;

public class VideoCommentDto {

    private int id;

    private String value;

    private String username;

    private int videoId;

    private Date date;

    public VideoCommentDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVideo() {
        return videoId;
    }

    public void setVideo(int video) {
        this.videoId = video;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
