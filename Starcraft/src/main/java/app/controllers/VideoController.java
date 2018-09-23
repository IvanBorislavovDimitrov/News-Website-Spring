package app.controllers;

import app.models.Video;
import app.models.VideoComment;
import app.pages.Page;
import app.pages.PageVideos;
import app.services.api.FileUploadService;
import app.services.api.VideoService;
import app.utilities.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    public VideoController(VideoService videoService, FileUploadService fileUploadService) {
        this.videoService = videoService;
        this.fileUploadService = fileUploadService;
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
        List<Video> videos = this.videoService.getAll();

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
        Video video = this.videoService.getById(Integer.parseInt(videoId));

        model.addAttribute("video", video);
        model.addAttribute("comments", video.getVideoComments());

        return "main/videos/video";
    }

    @PostMapping(value = "/watchVideo/{videoId}")
    @PreAuthorize("isAuthenticated()")
    public String addCommentToVideo(@PathVariable(name = "videoId") String videoId, Model model, VideoComment videoComment) {
        
        return "redirect:/watchVideo/" + videoId;
    }
}
