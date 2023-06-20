package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.CustomExceptions.ApiException;

import com.chicmic.eNaukri.Dto.*;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    private final SkillsService skillsService;
    private final EducationService educationService;
    private final ExperienceService experienceService;
    private final ApplicationService applicationService;
    private final UserServiceImpl userService;
    private final UsersService usersService;
    private final SocialLinkService linkService;

    @GetMapping("{id}")
    public void getUser(){

    }
    @PostMapping("{id}/update-profile")
    public ResponseEntity<String> updateProfile(@Valid UsersDto user, @PathVariable("id") Long id, @RequestParam("resumeFile")MultipartFile resumeFile, @RequestParam("imgFile")MultipartFile imgFile) throws IOException {
        usersService.updateUser(user,imgFile,resumeFile);
        return ResponseEntity.ok("updated successfully");
    }
    @PostMapping("/update-profile")
    public ResponseEntity<String> updateAll(Users users) {
        //usersService.updateUser(user,imgFile,resumeFile);

        return ResponseEntity.ok("updated successfully");
    }
    @PostMapping("/create-preferences")
    public ApiResponse createPreference(Principal principal,@RequestBody Preference dto){
        Preference preference=usersService.createPreferences(principal, dto);
        return new ApiResponse("Your preferences have been saved",preference,HttpStatus.CREATED);
    }
    @GetMapping("/my-applications")
    public ResponseEntity<String> myApplications(Principal principal){
        applicationService.viewApplications(principal);
        return ResponseEntity.ok("list of your applications");
    }
    @PostMapping("add-skills")
    public ResponseEntity<String> selectUserSkills(
            @PathVariable("userId") Long userId,
            @RequestBody UserSkillDto dto) {
        dto.setUserId(userId);
        skillsService.addSkills(dto);
        return ResponseEntity.ok("Skills selected successfully.");
    }
    @PostMapping("/add-education")
    public ResponseEntity<String> selectUserEducation(Principal principal, @RequestBody UserEducationDto dto) {
        educationService.addEducation(dto,principal);
        return ResponseEntity.ok("Educational qualification added to the user");
    }
    @PostMapping("/add-experience")
    public ResponseEntity<String> selectExperience(@RequestBody UserExperienceDto dto,Principal principal) {
        experienceService.addExperience(principal,dto);
        return ResponseEntity.ok("Experience added to the user");
    }
    @PostMapping("apply/{jobId}")
    public ResponseEntity<String> apply(Principal principal, @PathVariable("jobId") Long jobId,
             ApplicationDto application) throws MessagingException, IOException {
        applicationService.applyForJob(application,principal,jobId);
        return ResponseEntity.ok("Successfully applied to the job");
    }
    @GetMapping("{id}/unsubscribe")
    public String unsubscribe(Principal principal){
        userService.changeAlerts(principal,false);
        return "Unsubscribed !";
    }
    @GetMapping("{id}/subscribe")
    public String subscribe(Principal principal){
        userService.changeAlerts(principal,true);
        return "Subscribed !";
    }
    @GetMapping("{id}/currentCompany")
    public String currentCompany(@PathVariable("id") Long id){
        return userService.findCurrentCompany(id);
    }
    @GetMapping("{jobId}/checkApplication")
    public String check(Principal p, @PathVariable("jobId") Long jobId){
        userService.checkJobForUser(p, jobId);
        return "";
    }
    @PostMapping("withdraw/{jobId}")
    public void withdraw(Principal principal, @PathVariable("jobId") Long jobId){
        userService.withdrawApxn(principal, jobId);
    }
    @GetMapping("number-applicants/{jobId}")
    public String numApplicants(@PathVariable Long jobId){
        return String.valueOf(applicationService.getNumApplicantsForJob(jobId));
    }
    @PostMapping("{userid}/addSocialLinks")
    public ResponseEntity<String> addSocialLinks(@PathVariable("userid") Long userId, @RequestBody SocialLinkDto dto){
        linkService.addSocialLinks(userId, dto,null);
        return ResponseEntity.ok("Added social links");
    }

    @DeleteMapping("delete-experience")
    public  ApiResponse delexp(Long expId){
        experienceService.deleteExperience(expId);
        return  new ApiResponse("deleted",null,HttpStatus.valueOf(204));
    }
    @DeleteMapping("delete-education")
    public ApiResponse deled(Long edId){
        educationService.deleteEducation(edId);
        return new ApiResponse("deleted",null,HttpStatus.valueOf(204));
    }
    @PostMapping("/create-profile")
    public ApiResponse makeProfile(Principal principal, @RequestBody UserProfileDto dto){
        UserProfile up=usersService.createProfile(dto, principal);//,imgFile);
        return new ApiResponse("Profile has been set",up,HttpStatus.CREATED);
    }

}
