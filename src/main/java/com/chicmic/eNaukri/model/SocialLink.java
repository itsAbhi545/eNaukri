package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
//    @Pattern(regexp = RegEx.LINKEDIN,message ="Invalid linkedIn link" )
    private String linkedIn;
//    @Pattern(regexp = RegEx.TWITTER,message = "Invalid twitter link")
    private String twitter;
    private String others;
//    @Pattern(regexp = RegEx.WEBSITE,message = "Invalid website link")
    private String facebook;

    @OneToOne
    @JsonIgnore
    private Users user;
}
