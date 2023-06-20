package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor

public class
Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;
    private String name;
    @Column(unique = true)
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
    private boolean approved;

//    @OneToMany(mappedBy = "postFor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    private Set<Job> jobList=new HashSet<>();

    @OneToMany(mappedBy = "company",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Employer> employerSet =new HashSet<>();


}
