package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.chicmic.eNaukri.util.FileUploadUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {
    private final UsersRepo usersRepo;
    private final JavaMailSender javaMailSender;


    public Users getUserByEmail(String email) {
        return usersRepo.findByEmail(email);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

    public Users getUserById(Long userId){return usersRepo.findByUserId(userId);}


    public Users register(Users newUser) {
        String uuid= UUID.randomUUID().toString();
        newUser.setUuid(uuid);
        newUser.setPassword(passwordEncoder().encode(newUser.getPassword()));
        usersRepo.save(newUser);
        return newUser;
    }

    @Async public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("harmanjyot.singh@chicmic.co.in");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    public void updateUser(@Valid UsersDto user, @RequestParam MultipartFile imgFile,
                           @RequestParam MultipartFile resumeFile) throws IOException {
        Users existingUser=usersRepo.findByEmail(user.getEmail());
        ObjectMapper mapper = CustomObjectMapper.createObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.updateValue(existingUser,user);
        existingUser.getUserProfile().setPpPath(FileUploadUtil.imageUpload(imgFile));
        existingUser.getUserProfile().setCvPath(FileUploadUtil.resumeUpload(resumeFile));
        existingUser.setUpdatedAt(LocalDateTime.now());
        usersRepo.save(existingUser);
    }

}
