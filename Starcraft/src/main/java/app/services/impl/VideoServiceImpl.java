package app.services.impl;

import app.models.Video;
import app.repositories.GenericRepository;
import app.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {

    private final GenericRepository<Video> videoRepository;

    @Autowired
    public VideoServiceImpl(@Qualifier("Video") GenericRepository<Video> videoRepository) {
        this.videoRepository = videoRepository;
    }


    @Override
    public void save(String name) {
        Video video = new Video();
        if (name == null) {
            throw new IllegalArgumentException("Name mustn't be null");
        }
        video.setName(name);

        this.videoRepository.save(video);
    }
}
