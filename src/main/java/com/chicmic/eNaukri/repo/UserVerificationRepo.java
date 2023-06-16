package com.chicmic.eNaukri.repo;


import com.chicmic.eNaukri.model.UserVerification;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepo extends JpaRepository<UserVerification, Long> {
    UserVerification findByToken(String token);
    UserVerification findByUsers(Users Users);
}
