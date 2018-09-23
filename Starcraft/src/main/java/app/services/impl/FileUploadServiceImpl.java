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

    private static final String AVATARS_DIRECTORY = "images/avatars";

    @Override
    public void saveAvatar(MultipartFile avatar, String username) throws IOException {
        if (avatar.getOriginalFilename() != null) {
            String extension = avatar.getOriginalFilename()
                    .substring(avatar.getOriginalFilename().lastIndexOf("."));
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
}
