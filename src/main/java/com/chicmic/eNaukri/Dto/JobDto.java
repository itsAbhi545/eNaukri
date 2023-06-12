package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.JobSkills;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data public class JobDto {
    private Long userId;
    private String jobTitle;
    private String jobDesc;
    private boolean active;
    private String jobType;
    private String location;
    private String remoteHybridOnsite;
    private LocalDate postedOn;
    private LocalDate updatedOn;
    private LocalDate expiresAt;
    private List<String> skillsList;
}
