package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class
Employer {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String fullName;
    private String designation;

    private String ppPath;

//    @Column(columnDefinition = "boolean default false")
     @Column(columnDefinition = "BIT DEFAULT 0")
     private Boolean isApproved = false;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users users;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Company company;
    @OneToMany(mappedBy = "employer", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Job> jobList=new HashSet<>();



}
