package app.services.impl;

import app.services.api.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final String AVATARS_DIRECTORY = "content/avatars";
    private static final String VIDEO_DIRECTORY = "content/videos";
    private static final String VIDEO_THUMBNAILS = "content/videos/thumbnails";

    @Override
    public void saveAvatar(MultipartFile avatar, String username) throws IOException {
        if (avatar.getOriginalFilename() != null) {
            String extension = avatar.getOriginalFilename()
                    .substring(avatar.getOriginalFilename().lastIndexOf("."));
            if (!extension.equals(".jpg") && !extension.equals(".png")) {
                throw new IllegalArgumentException("Unsupported image format!\n" + extension);
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
    public void saveVideo(MultipartFile video, String name, MultipartFile thumbnail, String thumbnailName) throws IOException {
        if (video.getOriginalFilename() != null && thumbnail.getOriginalFilename() != null) {
            String videoExtension = video.getOriginalFilename()
                    .substring(video.getOriginalFilename().lastIndexOf("."));
            if (!videoExtension.equals(".mp4")) {
                throw new IllegalArgumentException("Unsupported video format!\n" + videoExtension);
            }
            Path fileNameAndPath = Paths.get(VIDEO_DIRECTORY, name + videoExtension);
            Files.write(fileNameAndPath, video.getBytes());

            String thumbnailExtension = thumbnail.getOriginalFilename()
                    .substring(thumbnail.getOriginalFilename().lastIndexOf("."));
            if (!thumbnailExtension.equals(".jpg") && !thumbnailExtension.equals(".png")) {
                throw new IllegalArgumentException("Unsupported image format!\n" + thumbnailExtension);
            }
            Path thumbnailPath = Paths.get(VIDEO_THUMBNAILS, thumbnailName);
            Files.write(thumbnailPath, thumbnail.getBytes());
        }
    }
}
