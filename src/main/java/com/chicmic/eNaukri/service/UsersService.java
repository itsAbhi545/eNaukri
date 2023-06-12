package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.UserCompany;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.UserCompanyRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.chicmic.eNaukri.util.FileUploadUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepo usersRepo;
    private final JavaMailSender javaMailSender;
    private final FileUploadUtil fileUploadUtil;

    public Users getUserByEmail(String email) {
        return usersRepo.findByEmail(email);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

    public Users getUserById(Long userId){return usersRepo.findByUserId(userId);}


    public Users register(@Valid Users dto) {
        String uuid= UUID.randomUUID().toString();
        Users newUser = CustomObjectMapper.convertDtoToObject(dto,Users.class);
//        newUser.setPpPath(fileUploadUtil.imageUpload(imgFile));
//        newUser.setCvPath(fileUploadUtil.resumeUpload(resumeFile));
        newUser.setUuid(uuid);
        // Generate OTP
        String otp =Integer.toString(new Random().nextInt(999999));
        // Send OTP to user's email
        String subject = "OTP for user registration";
        String message = "Your OTP is: " + otp;
        newUser.setPassword(passwordEncoder().encode(dto.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setOtp(otp);
        newUser.setVerified(false);
        usersRepo.save(newUser);
        System.out.println(newUser.getCvPath());
        sendEmailForOtp(newUser.getEmail(), subject, message);
        return newUser;
    }

    @Async public void sendEmailForOtp(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("harmanjyot.singh@chicmic.co.in");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
    public boolean verify(Long userId, String otp) {
        // Get user by id
        Users user = usersRepo.findById(userId).get();
        if (user == null) {
            return false;
        }
        // Check if OTP is correct
        if (user.getOtp().equals(otp)) {
            // Update user's OTP status to verified
            user.setVerified(true);
            usersRepo.save(user);
            return true;
        } else {
            return false;
        }
    }
    public void updateUser(@Valid UsersDto user, @RequestParam MultipartFile imgFile,
                           @RequestParam MultipartFile resumeFile) throws IOException {
        Users existingUser=usersRepo.findByEmail(user.getEmail());
        ObjectMapper mapper = CustomObjectMapper.createObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.updateValue(existingUser,user);
        existingUser.setPpPath(fileUploadUtil.imageUpload(imgFile));
        existingUser.setCvPath(fileUploadUtil.resumeUpload(resumeFile));
        existingUser.setUpdatedAt(LocalDateTime.now());
        usersRepo.save(existingUser);
    }
}
