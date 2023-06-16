package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class UserProfileDto {
    private String currentCompany;
    private String cvPath;
    private String bio;
    private String ppPath;
    private Preference preference;
    private List<Education> educationList;
    private List<Long> skillsList;
    private List<Experience> experienceList;
}
