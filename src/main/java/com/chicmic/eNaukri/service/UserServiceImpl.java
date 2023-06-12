package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.controller.UserController;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final ExperienceRepo experienceRepo;
    private final SkillsRepo skillsRepo;
    private final CompanyRepo companyRepo;
    private final UserTokenRepo tokenRepo;
    private final JobSkillsRepo jobSkillsRepo;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    JavaMailSender javaMailSender;

    public void saveUUID(UserToken userToken) {
        tokenRepo.save(userToken);
    }

    public Users getUserByEmail(String username) {
        return usersRepo.findByEmail(username);
    }
    public void saveUser(Users user) {
        usersRepo.save(user);
    }

    public Users findUserFromUUID(String token) {
        UserToken userToken= tokenRepo.findByToken(token);
        if(userToken==null){
//            throw new ApiException(HttpStatus.BAD_REQUEST,"Null or invalid token.");
            return null;
        }
//        return usersRepo.findById(userToken.getUserr().getUserId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"User doesn't exist"));
    return userToken.getUserr();
    }
    @Transactional
    public void deleteToken(String uuid) {
        tokenRepo.deleteUserTokenByToken(uuid);
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader=request.getHeader("Authorization").substring(7);
        System.out.println(authHeader);
        tokenRepo.deleteUserTokenByToken(authHeader);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user= usersRepo.findByEmail(username);
        if(user==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"User does not exist");
        }
        if(user.isVerified()==false){
            throw new ApiException(HttpStatus.UNAUTHORIZED,"User is not verified");
        }
        Collection<Authority> authorities=new ArrayList<>();
        authorities.add(new Authority("USER"));
        return new User(user.getEmail(),user.getPassword(),authorities);
    }

    public String findCurrentCompany(Long id) {
        Users users=usersRepo.findById(id).get();
       return experienceRepo.findByExpUserAndCurrentlyWorking(users,true).getExCompany().getCompanyName();
    }

    public void changeAlerts(Long id, boolean b) {
        Users temp=usersRepo.findById(id).get();
        temp.setEnableNotification(b);
        usersRepo.save(temp);
    }

    public boolean checkJobForUser(Long id, Long jobId) {
        Users temp=usersRepo.findById(id).get();
        Job job=jobRepo.findById(jobId).get();
        return applicationRepo.existsByApplicantIdAndJobId(temp,job);
    }

    public void withdrawApxn(Long id, Long jobId) {
        Users temp=usersRepo.findById(id).get();
        Job job=jobRepo.findById(jobId).get();
        Application application= applicationRepo.findByApplicantIdAndJobId(temp,job);
        application.setWithdraw(true);
        applicationRepo.save(application);
        job.setNumApplicants(job.getNumApplicants()-1);
        applicationRepo.delete(application);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

}
