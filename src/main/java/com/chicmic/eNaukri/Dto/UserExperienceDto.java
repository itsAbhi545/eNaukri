package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.Company;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data public class UserExperienceDto {
    private String role;
    private String roleDesc;
    private boolean currentlyWorking;
    private LocalDate joinedOn;
    private LocalDate endedOn;
    private Long companyId;
}
