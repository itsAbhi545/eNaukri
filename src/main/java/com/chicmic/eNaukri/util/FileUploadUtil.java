package com.chicmic.eNaukri.util;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Log4j
public class FileUploadUtil {
    @Value("${cvPath}")
   public static  String resumePath;
    public static String imagePath;

    @Value("${imagePath}")
    public void setNameStatic(String imagePath){
        FileUploadUtil.imagePath = imagePath;
    }


    public static  String resumeUpload(MultipartFile resumeFile) throws IOException {
        String cvPath = null;
        if (!resumeFile.isEmpty()) {
            String resumeFolder = resumePath;
            byte[] resumeFileBytes = resumeFile.getBytes();
            Path resumePath = Paths.get(resumeFolder + resumeFile.getOriginalFilename());
            Files.write(resumePath, resumeFileBytes);
            cvPath = "/static/assets/files/" + resumeFile.getOriginalFilename();
        }
        return cvPath;
    }

    public static String imageUpload(MultipartFile resumeFile) throws IOException {

        String ppPath = null;
        if (resumeFile != null && !resumeFile.isEmpty()) {
            String imgFolder = imagePath;
            byte[] imgFileBytes = resumeFile.getBytes();
            Path imgPath = Paths.get(imgFolder + resumeFile.getOriginalFilename());
            log.error("Image Path: " + imgPath + " " + imagePath);
            Files.write(imgPath, imgFileBytes);
            ppPath = "/static/assets/files/" + resumeFile.getOriginalFilename();
        }
        return ppPath;
    }
//    public  static String imageUpload(MultipartFile resumeFile, String Path) throws IOException {
//        String ppPath = null;
//        if (!resumeFile.isEmpty()) {
//            String imgFolder = Path;
//            byte[] imgFileBytes = resumeFile.getBytes();
//            Path imgPath = Paths.get(imgFolder + resumeFile.getOriginalFilename());
//            Files.write(imgPath, imgFileBytes);
//            ppPath = "/static/assets/files" + resumeFile.getOriginalFilename();
//        }
//        return ppPath;
//    }
}

