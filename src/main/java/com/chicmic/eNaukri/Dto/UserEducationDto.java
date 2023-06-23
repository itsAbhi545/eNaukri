package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.Education;
import lombok.Data;

import java.time.LocalDate;

@Data public class UserEducationDto {
    private LocalDate startFrom;
    private LocalDate endOn;
    private String universityName;
    private boolean student;
    private String degree;
    private String majors;
}
