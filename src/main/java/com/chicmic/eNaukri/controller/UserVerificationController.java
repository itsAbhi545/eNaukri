//package com.chicmic.eNaukri.controller;
//
//import com.chicmic.eNaukri.CustomExceptions.ApiException;
//import com.chicmic.eNaukri.model.PasswordResetToken;
//import com.chicmic.eNaukri.model.UserVerification;
//import com.chicmic.eNaukri.model.Users;
//import com.chicmic.eNaukri.service.EmailService;
//import com.chicmic.eNaukri.service.UserVerificationService;
//import com.chicmic.eNaukri.service.UsersService;
//import jakarta.mail.MessagingException;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.UnsupportedEncodingException;
//import java.time.LocalDateTime;
//
//@RestController
//@RequiredArgsConstructor
//public class UserVerificationController {
//    private final UsersService usersService;
//    @PostMapping("/user/verify")
//    public void userVerification(@RequestParam(name = "email", required = false) String email) throws MessagingException, UnsupportedEncodingException {
//        Users users = usersService.getUserByEmail(email);
//        if (users!= null) {
//
//        }else {
//            throw new ApiException(HttpStatus.BAD_REQUEST,"User Does Not Exist");
//        }
//    }
//    @PostMapping("/link/verify/{token}/{uuid}")
//    public String Enter(@PathVariable("token") String token, @PathVariable("uuid") String uuid) {
//        UserVerification userVerification = userVerificationService.findByToken(token);
//        if (userVerification == null || userVerification.getExpiryDate().isBefore(LocalDateTime.now())) {
//            //userVerification.delete(userVerification);
//            throw new ApiException(HttpStatus.BAD_REQUEST,"Invalid Token");
//        }
//        Users users = usersService.getUserByUuid(uuid);
//        if(users == userVerification.getUsers()){
//            return "success";
//        }
//        return "Invalid Request";
//    }
//
//}
