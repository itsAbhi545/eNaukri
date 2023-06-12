package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class
Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    private String companyName;
    private String email;
    private String phoneNumber;
    private String designation;

    private LocalDate foundedIn;
    private String about;
    private String ppPath;
    private String zipCode;
    private String address;
    private String gstId;
    private String iso;
    private String website;

    @OneToMany(mappedBy = "postFor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Job> jobList=new ArrayList<>();

    @OneToMany(mappedBy = "employerCompany",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employer> employerList=new ArrayList<>();


}
