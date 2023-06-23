package com.chicmic.eNaukri.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ApplicationDto {
    private int noticePeriod;
    private String fullName;
    private String email;
    private String phoneNumber;
    private MultipartFile resumeFile;
}
