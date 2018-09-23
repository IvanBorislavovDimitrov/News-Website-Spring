package app.services.impl;

import app.models.Video;
import app.repositories.GenericRepository;
import app.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        video.setDate(date);
        this.videoRepository.save(video);
    }

    @Override
    public List<Video> getAll() {
        return this.videoRepository.getAll();
    }

    @Override
    public Video getById(int id) {
        return this.videoRepository.getById(id);
    }
}
