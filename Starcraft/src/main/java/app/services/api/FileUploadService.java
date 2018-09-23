package app.services.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    void saveAvatar(MultipartFile avatar, String username) throws IOException;

    void deleteOldAvatar(String avatarName) throws IOException;

    void saveVideo(MultipartFile video, String name) throws IOException;
}
