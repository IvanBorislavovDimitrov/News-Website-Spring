package app.services.impl;


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
}
