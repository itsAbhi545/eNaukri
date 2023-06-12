package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private String salary;
    private Long yoe;
    private String remoteHybridOnsite;
    @OneToOne
    private Users userPreferences;
}
