package com.chicmic.eNaukri.model;

import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter@Setter
@TrimAll
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="categories_skills",joinColumns = @JoinColumn(name="categories_id"), inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Set<Skills> categorySkills=new HashSet<>();

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    List<Job> jobList=new ArrayList<>();
}
