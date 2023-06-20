package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.UserVerification;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.UserVerificationRepo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVerificationService {
    @Value("${serverAddress}")
    String serverAddress;
    private final EmailService emailService;
    private final UserVerificationRepo verificationRepo;
    public void createUserVerificationToken(Users user) throws MessagingException, UnsupportedEncodingException {
        UUID token= UUID.randomUUID();
        Long id = verificationRepo.findByUsers(user).getId().describeConstable().orElse(null);
        UserVerification verification = UserVerification.builder()
                .id(id)
                .users(user)
                .token(token.toString())
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        verificationRepo.save(verification);
        String url=serverAddress + "/verify?v="+token+"/"+user.getUuid();
        String subject = "Please Verify Your Email Address";
        String message = String.format( """
                <p>Click Below To Verify Your Email</p>
                <a href=%s style="display: inline-block; text-decoration: none; background: #10A37F; border-radius: 3px; color: white; font-family: Helvetica, sans-serif; font-size: 16px; line-height: 24px; font-weight: 400; padding: 12px 20px 11px; margin: 0px" target="_blank" rel="noreferrer">Verify Your Email</a>   
                """,url);
        emailService.sendEmail(user.getEmail(),subject, message);
    }
    public UserVerification findByUser(Users users){
        return verificationRepo.findByUsers(users);
    }
    public UserVerification findByToken(String token){
        return verificationRepo.findByToken(token);
    }
}
