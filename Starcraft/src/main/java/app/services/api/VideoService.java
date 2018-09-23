package app.services.api;

import app.dtos.video_dtos.VideoCommentDto;
import app.dtos.video_dtos.VideoDto;
import app.models.Video;

import java.util.List;

public interface VideoService {

    void save(String name);

    List<VideoDto> getAll();

    VideoDto getById(int id);

    void addComment(int videoId, VideoCommentDto videoComment, String username);
}
