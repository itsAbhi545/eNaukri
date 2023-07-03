package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.PasswordResetTokenRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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
    public PasswordResetToken findByToken(String token){
        return resetTokenRepo.findByToken(token);
    }
    public void delete(PasswordResetToken passwordResetToken){
        resetTokenRepo.delete(passwordResetToken);
    }
    public PasswordResetToken findByUser(Users user){ return resetTokenRepo.findByUser(user);}
    public String sendEmailForPasswordReset(String email)
            throws MessagingException, UnsupportedEncodingException {
        Users user = usersRepo.findByEmail(email);
        if(user==null) {
            throw new ApiException(HttpStatus.FORBIDDEN,"The email you entered is invalid or the user doesn't exist");
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Integer number=new Random().nextInt(999999);
        String otp=String.format("%06d", number);
        PasswordResetToken oldToken=findByUser(user);
        if(oldToken!=null){
            oldToken.setOtp(otp);
            oldToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
            resetTokenRepo.save(oldToken);
        }
//        String otp =Integer.toString(new Random().nextInt(999999));
        else if (oldToken==null) {
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser(user);
            passwordResetToken.setOtp(otp);
            passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
            resetTokenRepo.save(passwordResetToken);
        }
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
        return otp;
    }
    public String verifyOtp(String otp,String email){
        Users user=usersRepo.findByEmail(email);
        PasswordResetToken tokenObject=findByUser(user);
        if(user==null||tokenObject==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"Invalid otp or user");
        }
        String savedOtp=tokenObject.getOtp();
        if(savedOtp==null){
            throw new ApiException(HttpStatus.FORBIDDEN,"Cannot use the same otp twice make a new request");
        }
        if(user==null||tokenObject==null||!savedOtp.equals(otp)){
            throw new ApiException(HttpStatus.NOT_FOUND,"Invalid otp or user");
        }
        if(LocalDateTime.now().isAfter(tokenObject.getExpiryDate())){
            resetTokenRepo.delete(tokenObject);
            throw new ApiException(HttpStatus.FORBIDDEN,"The Reset token has expired make a new request");
        }
        tokenObject.setToken(UUID.randomUUID().toString());
        tokenObject.setOtp(null);
        resetTokenRepo.save(tokenObject);
        return tokenObject.getToken();
    }
    public Users resetPassword(String token, String newPassword, String confirmPassword){
        PasswordResetToken tokenObject= resetTokenRepo.findByToken(token);
        if(tokenObject==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"Invalid token");
        }
        Users user=tokenObject.getUser();
        if(user==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"Invalid token or user");
        }
        if(!newPassword.equals(confirmPassword)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Password fields do not match");
        } else if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder().encode(newPassword));
            usersRepo.save(user);
        }
        resetTokenRepo.delete(tokenObject);
        return user;
    }
}
