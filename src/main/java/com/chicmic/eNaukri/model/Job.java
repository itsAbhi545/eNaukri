package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @CreationTimestamp
    private LocalDate postedOn;
    @UpdateTimestamp
    private LocalDate updatedOn;
    private LocalDate expiresAt;
    private Float  minYear;
    private Float maxYear;
    private Float minSalary;
    private Float maxSalary;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean active;
    private int numApplicants;

//mappings
    @OneToMany(mappedBy = "jobId", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Application> applicationList =new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<JobSkills> jobSkillsList =new ArrayList<>();


    @ManyToOne
    private Employer employer;
}
