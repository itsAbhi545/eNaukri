package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;
    @Column(unique = true)
    private String skillName;

    @OneToMany(mappedBy = "skills",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserSkills> userSkillsList=new ArrayList<>();

    @OneToMany(mappedBy = "skill",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<JobSkills> jobSkillsList=new ArrayList<>();
    @ManyToOne
    Categories categories;
}
