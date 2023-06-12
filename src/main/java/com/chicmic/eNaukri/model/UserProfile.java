package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//@TrimAll
@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JsonIgnore
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private Preference preference;

    @OneToMany(mappedBy = "edUser", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private List<Education> educationList=new ArrayList<>();

    @OneToMany(mappedBy = "applicantId", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Application> applicationList=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    private List<UserSkills> userSkillsList=new ArrayList<>();
    @OneToMany(mappedBy = "expUser",orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Experience> experienceList=new ArrayList<>();


}
