package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpId;
    private String token;
    @ManyToOne
    private Users fUser;
    private LocalDateTime expiryTime;

    public boolean isExpired(){
        return this.expiryTime.isBefore(LocalDateTime.now());
    }
}
