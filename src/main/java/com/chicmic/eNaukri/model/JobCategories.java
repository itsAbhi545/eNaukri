package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@RequiredArgsConstructor
public class JobCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "jobCategories",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Skills> categorySkills=new ArrayList<>();
    @ManyToMany(mappedBy = "jobCategories", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Job> jobList=new ArrayList<>();
}
