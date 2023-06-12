package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String jobTitle;
    private String location;
    private String jobType;
    private String remoteHybridOnsite;
    private String jobDesc;
    private LocalDate postedOn;
    private LocalDate updatedOn;
    private LocalDate expiresAt;
    private boolean active;
    private int numApplicants;

//mappings
    @OneToMany(mappedBy = "jobId", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Application> applicationList =new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<JobSkills> jobSkillsList =new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Company postFor;

}
