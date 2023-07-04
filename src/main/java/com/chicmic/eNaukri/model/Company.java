package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class
Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate foundedIn;

    private String about;

    private String ppPath;

    private String zipCode;

    private String address;

    private String gstId;

    private String iso;

    private String website;
    @Column(columnDefinition = "BIT DEFAULT 0")
    private boolean approved;
    private String uuid;
    private String size;
    private Double completionStatus;

//    @OneToMany(mappedBy = "postFor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    private Set<Job> jobList=new HashSet<>();


    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "company",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Employer> employerSet =new HashSet<>();


    public Company(Company company) {
        this.companyId = company.getCompanyId();
        this.name = company.getName();
        this.foundedIn = company.getFoundedIn();
        this.about = company.about;
        this.ppPath = company.ppPath;
        this.zipCode = company.zipCode;
        this.address = company.address;
        this.gstId = company.gstId;
        this.iso = company.iso;
        this.website = company.website;
        this.approved = company.approved;
        this.uuid = company.uuid;
        this.size = company.size;
        this.completionStatus = company.completionStatus;
        this.users = company.users;
        this.employerSet = company.employerSet;
    }
}
