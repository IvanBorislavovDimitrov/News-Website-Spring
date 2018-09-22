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

    private static final String AVATARS_DIRECTORY = "src/main/resources/static/images/avatars";

    @Override
    public void saveAvatar(MultipartFile avatar) throws IOException {
        Path fileNameAndPath = Paths.get(AVATARS_DIRECTORY, avatar.getOriginalFilename());
        Files.write(fileNameAndPath, avatar.getBytes());
    }
}
