package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class UserSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSkillId;

    @ManyToOne
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JsonIgnore
    private Skills skills;
}
