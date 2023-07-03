package com.chicmic.eNaukri.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
=======
import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
>>>>>>> origin/Employer-SignUp/Login
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
<<<<<<< HEAD
@RequiredArgsConstructor
=======
@TrimAll
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> origin/Employer-SignUp/Login
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "categories",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
            @JsonIgnore
    List<Skills> categorySkills=new ArrayList<>();

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    List<Job> jobList=new ArrayList<>();
}
