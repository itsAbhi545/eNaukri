package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken,String> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByOtp(String otp);
    PasswordResetToken findByUser(Users user);

}
