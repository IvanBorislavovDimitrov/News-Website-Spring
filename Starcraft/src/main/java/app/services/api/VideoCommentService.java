package app.services.api;

import app.dtos.video_dtos.VideoCommentDto;
import app.models.VideoComment;

public interface VideoCommentService {

    void save(VideoComment videoComment);

    void update(int commentId, VideoCommentDto videoCommentDto);

    void delete(int commentId);
}
