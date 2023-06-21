package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter

@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "categories",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Skills> categorySkills=new ArrayList<>();
    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Job> jobList=new ArrayList<>();
}
