package app.controllers;

import app.dtos.video_dtos.VideoCommentDto;
import app.dtos.video_dtos.VideoDto;
import app.pages.PageVideos;
import app.services.api.FileUploadService;
import app.services.api.VideoCommentService;
import app.services.api.VideoService;
import app.utilities.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class VideoController {

    private static final int MAX_VIDEO_ON_PAGE = 6;
    private final VideoService videoService;
    private final FileUploadService fileUploadService;
    private final VideoCommentService videoCommentService;

    @Autowired
    public VideoController(VideoService videoService, FileUploadService fileUploadService, VideoCommentService videoCommentService) {
        this.videoService = videoService;
        this.fileUploadService = fileUploadService;
        this.videoCommentService = videoCommentService;
    }

    @GetMapping(value = "/uploadVideo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadUploadVideoPage() {

        return "main/videos/uploadVideo";
    }

    @PostMapping(value = "/uploadVideo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String uploadVideo(@RequestParam(name = "video") MultipartFile video,
                              @RequestParam(name = "name") String name, Model model) {
        if (video.getOriginalFilename() == null ||
                video.getOriginalFilename().trim().equals("") ||
                name == null || name.equals("")) {
            model.addAttribute("invalidForm", true);

            return "main/videos/uploadVideo";
        }

        this.videoService.save(name);
        try {
            this.fileUploadService.saveVideo(video, name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping(value = "/videos")
    public String loadVideosPage(Model model, @RequestParam(name = "page", defaultValue = "1") String page) {
        List<VideoDto> videos = this.videoService.getAll();

        int size = videos.size();
        int pagesCount = (int) Math.ceil(size / (double) MAX_VIDEO_ON_PAGE);
        PageVideos[] pages = new PageVideos[pagesCount];
        int count = 0;
        Helper.addVideosToPages(videos, pages, count, MAX_VIDEO_ON_PAGE);

        int requiredPage = Integer.parseInt(page) - 1;

        if (pagesCount == 0 || videos.size() == 0) {
            model.addAttribute("areThereNews", false);

            return "/main/news/search";
        }

        model.addAttribute("videos", pages[requiredPage].getVideos());
        model.addAttribute("pages", pages);
        model.addAttribute("pageNumber", requiredPage + 1);
        model.addAttribute("maxPages", pagesCount + 1);
        model.addAttribute("minPages", 0);

        return "main/videos/videos";
    }

    @GetMapping(value = "/watchVideo/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String loadSeeFullVideoPage(@PathVariable(name = "videoId") String videoId, Model model) {
        VideoDto video = this.videoService.getById(Integer.parseInt(videoId));

        model.addAttribute("video", video);
        model.addAttribute("comments", video.getVideoComments());

        return "main/videos/video";
    }

    @PostMapping(value = "/watchVideo/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String addCommentToVideo(@PathVariable(name = "videoId") String videoId, Model model, VideoCommentDto videoComment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.videoService.addComment(Integer.parseInt(videoId), videoComment, username);

        return "redirect:/watchVideo/" + videoId;
    }

    @GetMapping(value = "/editVideoComment/{commentId}/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String loadEditVideoCommentPage(@PathVariable(value = "commentId") int commentId,
                                           @PathVariable(value = "videoId") int videoId,
                                           Model model) {
        String currentLoggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        VideoDto videoDto = this.videoService.getById(videoId);
        VideoCommentDto videoCommentDto = videoDto.getVideoComments().stream()
                .filter(c -> c.getId() == commentId)
                .findFirst()
                .orElse(null);
        if (!currentLoggedUser.equals(videoCommentDto.getUsername())) {
            return "main/notAllowed/forbidden";
        }

        model.addAttribute("comment", videoCommentDto);

        return "main/videos/editVideoComment";
    }

    @PostMapping(value = "/editVideoComment/{commentId}/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String editVideoComment(@PathVariable(value = "commentId") int commentId,
                                   @PathVariable(value = "videoId") int videoId,
                                   VideoCommentDto videoCommentDto) {
        this.videoCommentService.update(commentId, videoCommentDto);

        return "redirect:/watchVideo/" + videoId;
    }

    @GetMapping(value = "/deleteVideoComment/{commentId}/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String loadDeleteVideoCommentPage(@PathVariable(value = "commentId") int commentId,
                                             @PathVariable(value = "videoId") int videoId,
                                             Model model) {
        String currentLoggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        VideoDto videoDto = this.videoService.getById(videoId);
        VideoCommentDto videoCommentDto = videoDto.getVideoComments().stream()
                .filter(c -> c.getId() == commentId)
                .findFirst()
                .orElse(null);
        if (!currentLoggedUser.equals(videoCommentDto.getUsername())) {
            return "main/notAllowed/forbidden";
        }

        model.addAttribute("comment", videoCommentDto);

        return "main/videos/deleteVideoComment";
    }

    @PostMapping(value = "/deleteVideoComment/{commentId}/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String deleteVideoComment(@PathVariable(value = "commentId") int commentId,
                                   @PathVariable(value = "videoId") int videoId,
                                   VideoCommentDto videoCommentDto) {
        this.videoCommentService.delete(commentId);

        return "redirect:/watchVideo/" + videoId;
    }
}
