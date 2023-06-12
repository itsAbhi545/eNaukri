package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.PasswordResetTokenRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final JavaMailSender javaMailSender;
    private final PasswordResetTokenRepo resetTokenRepo;
    private final UsersRepo usersRepo;
    public PasswordResetToken findByToken(String token){
        return resetTokenRepo.findByToken(token);
    }
    public void delete(PasswordResetToken passwordResetToken){
        resetTokenRepo.delete(passwordResetToken);
    }
    public void sendEmailForPassword(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("bluflame.business@gmail.com", "eNaukri Site");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p style=\"color:blue;\"><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
        System.out.println("email sent");

    }
    public void createPasswordResetTokenForUser(Users user) throws MessagingException, UnsupportedEncodingException {
        PasswordResetToken passwordResetRequest = new PasswordResetToken();
        UUID token1= UUID.randomUUID();
        passwordResetRequest.setUser(user);
        passwordResetRequest.setToken(token1.toString());
        passwordResetRequest.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetTokenRepo.save(passwordResetRequest);
        String link1="http://localhost:8081/enterNewPassword?v="+token1+"/"+user.getUuid();
        sendEmailForPassword(user.getEmail(), link1);
    }
    public void changeUserPassword(Users user, String password) {
        user.setPassword(password);
        usersRepo.save(user);
    }


}
