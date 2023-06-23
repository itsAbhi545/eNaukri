package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import com.chicmic.eNaukri.model.JobSkills;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TrimAll
public class JobDto {
    private Long userId;
    private String jobTitle;
    private String jobDesc;
    private String jobType;
    private String location;
    private String remoteHybridOnsite;
    private LocalDate postedOn;
    private LocalDate updatedOn;
    private LocalDate expiresAt;
    private Float  minYear;
    private Float maxYear;
    private Float minSalary;
    private Float maxSalary;
    private List<String> skillsList;
    private List<String> otherSkills = new ArrayList<>();
    private List<Long> jobCategories;
}
