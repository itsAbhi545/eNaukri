package com.chicmic.eNaukri.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.UserVerification;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.EmailService;
import com.chicmic.eNaukri.service.UserVerificationService;
import com.chicmic.eNaukri.service.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;

import static com.chicmic.eNaukri.config.SecurityConstants.EXPIRATION_TIME;
import static com.chicmic.eNaukri.config.SecurityConstants.SECRET;

@RestController
@RequiredArgsConstructor
public class UserVerificationController {
    private final UsersService usersService;
    private final UserVerificationService userVerificationService;
//    @PostMapping("/user/verify")
//    public void userVerification(@RequestParam(name = "email", required = false) String email) throws MessagingException, UnsupportedEncodingException {
//        Users users = usersService.getUserByEmail(email);
//        if (users!= null) {
//            userVerificationService.createUserVerificationToken(users);
//        }else {
//            throw new ApiException(HttpStatus.BAD_REQUEST,"User Does Not Exist");
//        }
//    }
    @PostMapping("/link/verify/{token}/{uuid}")
    public String Enter(@PathVariable("token") String token, @PathVariable("uuid") String uuid) {
        UserVerification userVerification = userVerificationService.findByToken(token);
        if (userVerification == null || userVerification.getExpiryDate().isBefore(LocalDateTime.now())) {
            //userVerification.delete(userVerification);
            throw new ApiException(HttpStatus.BAD_REQUEST,"Invalid Token");
        }
        Users users = usersService.getUserByUuid(uuid);
        if(users == userVerification.getUsers()){
            return "success";
        }
        return "Invalid Request";
    }
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, @RequestParam("userUuid") String userUuid) {
        // Verify the JWT and user UUID
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("your_secret_key")
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            if (subject.equals(userUuid)) {
                // User and JWT are valid, perform account verification logic
                // ...
                return "Account verified successfully!";
            }
        } catch (Exception e) {
            // Handle JWT validation or verification failure
        }

        return "Invalid verification token or user UUID.";
    }
}

