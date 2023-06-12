package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
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
public class UserCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userCompanyId;

    private String employeeRole;
    private String roleDesc;
    private boolean currentlyWorking;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate joinedAt;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate leftAt;

    //mappings
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Company usersCompany;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Users companyUsers;
}
