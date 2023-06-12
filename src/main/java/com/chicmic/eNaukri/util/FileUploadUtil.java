package com.chicmic.eNaukri.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUploadUtil {
    @Value("${my.cvPath.string}")
    String resumePath;
    @Value("${my.imagePath.string}")
    String imagePath;

    public String resumeUpload(MultipartFile resumeFile) throws IOException {
        String cvPath = null;
        if (!resumeFile.isEmpty()) {
            String resumeFolder = resumePath;
            byte[] resumeFileBytes = resumeFile.getBytes();
            Path resumePath = Paths.get(resumeFolder + resumeFile.getOriginalFilename());
            Files.write(resumePath, resumeFileBytes);
            cvPath = "/static/assets/files" + resumeFile.getOriginalFilename();
        }
        return cvPath;
    }

    public String imageUpload(MultipartFile resumeFile) throws IOException {
        String ppPath = null;
        if (!resumeFile.isEmpty()) {
            String imgFolder = imagePath;
            byte[] imgFileBytes = resumeFile.getBytes();
            Path imgPath = Paths.get(imgFolder + resumeFile.getOriginalFilename());
            Files.write(imgPath, imgFileBytes);
            ppPath = "/static/assets/files" + resumeFile.getOriginalFilename();
        }
        return ppPath;
    }
}

