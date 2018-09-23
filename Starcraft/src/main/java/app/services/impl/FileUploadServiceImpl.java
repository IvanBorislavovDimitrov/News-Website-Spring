package app.services.impl;

import app.services.api.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final String AVATARS_DIRECTORY = "content/avatars";
    private static final String VIDEO_DIRECTORY = "content/videos";

    @Override
    public void saveAvatar(MultipartFile avatar, String username) throws IOException {
        if (avatar.getOriginalFilename() != null) {
            String extension = avatar.getOriginalFilename()
                    .substring(avatar.getOriginalFilename().lastIndexOf("."));
            if (!extension.equals(".jpg") || !extension.equals(".png")) {
                throw new IllegalArgumentException("Unsupported image format!");
            }
            Path fileNameAndPath = Paths.get(AVATARS_DIRECTORY, username + "_avatar" + extension);
            Files.write(fileNameAndPath, avatar.getBytes());
        }
    }

    @Override
    public void deleteOldAvatar(String avatarName) throws IOException {
        if (avatarName != null) {
            Path fileNameAndPath = Paths.get(AVATARS_DIRECTORY, avatarName);
            Files.delete(fileNameAndPath);
        }
    }

    @Override
    public void saveVideo(MultipartFile video, String name) throws IOException {
        if (video.getOriginalFilename() != null) {
            String extension = video.getOriginalFilename()
                    .substring(video.getOriginalFilename().lastIndexOf("."));
            if (!extension.equals(".mp4")) {
                throw new IllegalArgumentException("Unsupported video format!");
            }
            Path fileNameAndPath = Paths.get(VIDEO_DIRECTORY, name + extension);
            Files.write(fileNameAndPath, video.getBytes());
        }
    }
}
