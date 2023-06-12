package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken,String> {
    public PasswordResetToken findByToken(String token);
}
