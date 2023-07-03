package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.PasswordResetTokenRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final JavaMailSender javaMailSender;
    private final PasswordResetTokenRepo resetTokenRepo;
    private final UsersRepo usersRepo;
//    public PasswordResetToken findByToken(String token){
//        return resetTokenRepo.findByToken(token);
//    }
    public void delete(PasswordResetToken passwordResetToken){
        resetTokenRepo.delete(passwordResetToken);
    }
    public void sendEmailForPasswordReset(String email)
            throws MessagingException, UnsupportedEncodingException {
        Users user = usersRepo.findByEmail(email);
        if(user==null) {
            throw new ApiException(HttpStatus.FORBIDDEN,"The email you entered is invalid or the user doesn't exist");
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String otp =Integer.toString(new Random().nextInt(999999));
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(otp);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        helper.setFrom("bluflame.business@gmail.com", "eNaukri Site");
        helper.setTo(email);
        String subject = "Here's the OTP to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Use the OTP below to change your password:</p>"
                + "<p" + otp + "</p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
        System.out.println("email sent");
    }
    public String verifyOtp(String otp,String newPassword){
        PasswordResetToken tokenObject=resetTokenRepo.findByOtp(otp);
        Users user=tokenObject.getUser();
        if(user==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"Invalid otp or user");
        }
        if(LocalDateTime.now().isAfter(tokenObject.getExpiryDate())){
            throw new ApiException(HttpStatus.FORBIDDEN,"The Reset token has expired make a new request");
        }
        tokenObject.setToken(UUID.randomUUID().toString());
        return tokenObject.getToken();
    }
    public Users resetPassword(String token, String newPassword, String confirmPassword){
        PasswordResetToken tokenObject= resetTokenRepo.findByToken(token);
        Users user=tokenObject.getUser();
        if(user==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"Invalid otp or user");
        }
        if(!newPassword.equals(confirmPassword)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Password fields do not match");
        } else if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder().encode(newPassword));
        }
        return user;
    }
}
