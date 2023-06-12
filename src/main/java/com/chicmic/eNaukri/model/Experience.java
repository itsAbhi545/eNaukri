package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expId;
    private String role;
    private String roleDesc;
    private boolean currentlyWorking;
    private LocalDate joinedOn;
    private LocalDate endedOn;

    @ManyToOne
    @JsonIgnore
    private Users expUser;

    @ManyToOne
    @JsonIgnore
    private Company exCompany;
}
