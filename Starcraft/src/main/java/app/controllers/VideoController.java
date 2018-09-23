package app.controllers;

import app.models.Video;
import app.services.api.FileUploadService;
import app.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class VideoController {

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

        return "main/video/uploadVideo";
    }

    @PostMapping(value = "/uploadVideo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String uploadVideo(@RequestParam(name = "video") MultipartFile video,
                              @RequestParam(name = "name") String name, Model model) {
        if (video.getOriginalFilename() == null ||
                video.getOriginalFilename().trim().equals("") ||
                name == null || name.equals("")) {
            model.addAttribute("invalidForm", true);

            return "main/video/uploadVideo";
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
    public String loadVideosPage(Model model) {
        List<Video> videos = this.videoService.getAll();
        model.addAttribute("videos", videos);

        return "main/video/videos";
    }
}
