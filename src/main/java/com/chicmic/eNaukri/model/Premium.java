package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Premium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long premiumId;
    @CreationTimestamp
    private LocalDateTime premiumSince;
    private String transactionId;
    @UpdateTimestamp
    private LocalDateTime renewedOn;
    @OneToOne
    private Users userSubscription;
}
