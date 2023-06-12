package com.chicmic.eNaukri.model;

import com.chicmic.eNaukri.validation.RegEx;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull
    private String fullName;
    private String username;
    @Email
    @Pattern(regexp = RegEx.EMAIL)
    @NotNull
    private String email;
    @Pattern(regexp = RegEx.PASSWORD,message = "")
    @NotNull
    private String password;
    @Pattern(regexp = RegEx.PHONENUMBER,message = "")
    @NotNull
    private String phoneNumber;
    private String currentCompany;
    private String cvPath;
    private String bio;
    private String ppPath;
    private String otp;
    @UuidGenerator
    private String uuid;
    private String link;
    private boolean isVerified;
    private boolean enableNotification;

    //mappings

    @OneToOne(mappedBy = "userLinks", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private SocialLink socialLink;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,mappedBy = "userr")
    @JsonIgnore
    private Set<UserToken> userTokenSet=new HashSet<>();

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private  UserProfile userProfile;
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    private  Employer employerProfile;



}
