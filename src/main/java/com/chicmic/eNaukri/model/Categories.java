package com.chicmic.eNaukri.model;

import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@TrimAll
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
    @JsonIgnore
    List<Job> jobList=new ArrayList<>();
}
