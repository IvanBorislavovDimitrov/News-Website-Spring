package app.controllers;

import app.services.api.FileUploadService;
import app.services.api.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
                               @RequestParam(name = "name") String name) {
        this.videoService.save(name);
        try {
            this.fileUploadService.saveVideo(video, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
