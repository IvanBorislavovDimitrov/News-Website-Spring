package app.services.impl;

import app.dtos.video_dtos.VideoCommentDto;
import app.dtos.video_dtos.VideoDto;
import app.models.User;
import app.models.Video;
import app.models.VideoComment;
import app.repositories.GenericRepository;
import app.services.api.UserService;
import app.services.api.VideoCommentService;
import app.services.api.VideoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    private final GenericRepository<Video> videoRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final VideoCommentService videoCommentService;

    @Autowired
    public VideoServiceImpl(@Qualifier("Video") GenericRepository<Video> videoRepository, ModelMapper modelMapper, UserService userService, VideoCommentService videoCommentService) {
        this.videoRepository = videoRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.videoCommentService = videoCommentService;
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
    public List<VideoDto> getAll() {
        List<VideoDto> videoDtos = new ArrayList<>();
        this.videoRepository.getAll().forEach(v -> {
            VideoDto videoDto = new VideoDto();
            videoDto.setDate(v.getDate());
            videoDto.setDislikes(v.getDislikes());
            videoDto.setId(v.getId());
            videoDto.setLikes(v.getLikes());
            videoDto.setName(v.getName());
            v.getVideoComments().forEach(c -> {
                VideoCommentDto videoCommentDto = this.modelMapper.map(c, VideoCommentDto.class);
                videoDto.getVideoComments().add(videoCommentDto);
            });
            videoDtos.add(videoDto);
        });

        return videoDtos;
    }

    @Override
    public VideoDto getById(int id) {
        Video video = this.videoRepository.getById(id);
        VideoDto videoDto = new VideoDto();
        videoDto.setDate(video.getDate());
        videoDto.setDislikes(video.getDislikes());
        videoDto.setId(video.getId());
        videoDto.setLikes(video.getLikes());
        videoDto.setName(video.getName());

        video.getVideoComments().forEach(c -> {
            VideoCommentDto videoCommentDto = this.modelMapper.map(c, VideoCommentDto.class);
            videoCommentDto.setUsername(c.getUser().getUsername());
            videoDto.getVideoComments().add(videoCommentDto);
        });

        return videoDto;
    }

    @Override
    public void addComment(int videoId, VideoCommentDto videoComment, String username) {
        Video video = this.videoRepository.getById(videoId);
        videoComment.setUsername(username);

        VideoComment comment = this.modelMapper.map(videoComment, VideoComment.class);
        User user = this.userService.getByUsername(username);
        user.getVideoComments().add(comment);
        comment.setUser(user);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comment.setDate(date);

        video.getVideoComments().add(comment);
        comment.setVideo(video);

        this.userService.update(username);
        this.videoRepository.update(video);
    }
}
