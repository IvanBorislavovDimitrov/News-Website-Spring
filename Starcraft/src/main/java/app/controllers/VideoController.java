package app.controllers;

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
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "thumbnail") MultipartFile thumbnail, Model model) {
        if (video.getOriginalFilename() == null || thumbnail.getOriginalFilename() == null ||
        video.getOriginalFilename().trim().equals("") || thumbnail.getOriginalFilename().equals("") ||
                name == null || name.equals("")) {
            model.addAttribute("invalidForm", true);

            return "main/video/uploadVideo";
        }
        String thumbnailName = name + "_thumbnail" + thumbnail.getOriginalFilename()
                .substring(thumbnail.getOriginalFilename().lastIndexOf("."));

        this.videoService.save(name, thumbnailName);
        try {
            this.fileUploadService.saveVideo(video, name, thumbnail, thumbnailName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }
}
