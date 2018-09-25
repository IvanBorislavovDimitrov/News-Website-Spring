package app.services.impl;


import app.dtos.video_dtos.VideoCommentDto;
import app.models.VideoComment;
import app.repositories.GenericRepository;
import app.services.api.VideoCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoCommentServiceImpl implements VideoCommentService {

    private final GenericRepository<VideoComment> videoCommentGenericRepository;

    @Autowired
    public VideoCommentServiceImpl(GenericRepository<VideoComment> videoCommentGenericRepository) {
        this.videoCommentGenericRepository = videoCommentGenericRepository;
    }

    @Override
    public void save(VideoComment videoComment) {
        this.videoCommentGenericRepository.save(videoComment);
    }

    @Override
    public void update(int commentId, VideoCommentDto videoCommentDto) {
        VideoComment videoComment = this.videoCommentGenericRepository.getById(commentId);
        videoComment.setValue(videoCommentDto.getValue());

        this.videoCommentGenericRepository.update(videoComment);
    }

    @Override
    public void delete(int commentId) {
        VideoComment byId = this.videoCommentGenericRepository.getById(commentId);

        this.videoCommentGenericRepository.delete(byId);
    }
}
