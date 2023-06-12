package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class SocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long socialLinkId;

    private String linkedIn;
    private String twitter;
    private String others;
    private String website;

    @OneToOne
    private Company companyLinks;
    @OneToOne
    private Users userLinks;
}
