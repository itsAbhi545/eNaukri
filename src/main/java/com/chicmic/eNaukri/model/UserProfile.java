package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String fullName;
    private String currentCompany;
    private String cvPath;
    private String bio;
    private String ppPath;
    private boolean hasPremium;
    @OneToOne(mappedBy = "userSubscription", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private Premium premium;
    @OneToOne(mappedBy = "userPreferences", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private Preference preference;

    @OneToMany(mappedBy = "edUser", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private List<Education> educationList=new ArrayList<>();

    @OneToMany(mappedBy = "applicantId", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Application> applicationList=new ArrayList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    private List<UserSkills> userSkillsList=new ArrayList<>();
    @OneToMany(mappedBy = "expUser",orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Experience> experienceList=new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @JsonIgnore
    private Users users;
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Job> invitedJobs=new ArrayList<>();

}
