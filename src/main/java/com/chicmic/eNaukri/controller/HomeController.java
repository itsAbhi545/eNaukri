package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.Dto.UserProfileDto;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.service.*;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final UsersRepo usersRepo;
    private final UsersService usersService;
    private final UserServiceImpl userService;
    private final JobService jobService;
    private final PasswordResetService passwordResetService;
    private final ResumeGenerator resumeGenerator;
    private final SkillsService skillsService;

    @GetMapping
    public String homePage(){
        System.out.println("1");
        return "In Home Page";
    }
//    @GetMapping("login-page")
//    public String loginPage(){
//        return "in Login Page";
//    }
//    @PostMapping("login")
//    public String userLogin(@RequestBody Map<Object,Object> map){
//        return "login successful";
//    }
    @PostMapping("api/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse register(@Valid Users dto) {
        Users user=usersService.register(dto);
        return new ApiResponse("user created",user,HttpStatus.CREATED );
    }
    @PostMapping("updateProfile")
    public void updateProfile(UsersDto user, @RequestParam(value="resumeFile",required = false)MultipartFile resumeFile, @RequestParam(value="imgFile",required = false)MultipartFile imgFile) throws IOException {
        usersService.updateUser(user,imgFile,resumeFile);
    }
    @GetMapping("logout-user")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        userService.logout(request,response);
        return "Logout Successful";
    }
    @GetMapping("search-skills")
    public List<Skills> displaySkills(@RequestParam String query){
        return skillsService.findBySkillName(query);
    }

    @GetMapping("jobs")
    public List<Job> displayJobs(@RequestParam(required = false,name = "q") String query,
                                 @RequestParam(required = false,name = "location") String location,
                                 @RequestParam(required = false,name = "type") String jobType,
                                 @RequestParam(required = false,name = "postedOn") String postedOn,
                                 @RequestParam(required = false,name = "remoteHybridOnsite") String remoteHybridOnsite,
                                 @RequestParam(required = false,name = "yoe") Integer yoe,
                                 @RequestParam(required = false,name = "salary") Integer salary,
                                 @RequestParam(required = false,name = "skills") List<Long> skillIds){
        return jobService.displayFilteredPaginatedJobs(query,location,jobType,postedOn,remoteHybridOnsite,yoe,salary,skillIds);
    }

    @GetMapping("{jobId}/listInterestedApplicants")
    public Collection<?> listInterestedApplicants(@PathVariable("jobId")Long jobId){
        return jobService.listInterestedApplicants(jobId);
    }
    @PostMapping("password-reset-request")
    public ResponseEntity<String> sendPasswordResetOtp(@RequestParam String email) throws
            MessagingException, UnsupportedEncodingException {
        passwordResetService.sendEmailForPasswordReset(email);
        return ResponseEntity.ok("Email has been sent");
    }
    @PostMapping("reset-password")
    public ApiResponse setNewPassword(@RequestParam String token,@RequestParam String newPassword){
        Users user=passwordResetService.resetPassword(token, newPassword);
        return new ApiResponse("Password Reset successfully",user,HttpStatus.ACCEPTED);
    }
    @GetMapping("/{userId}/download-pdf")
    public String downloadPDF(HttpServletResponse response, @PathVariable Long userId) throws IOException, DocumentException {
        Users users=usersService.getUserById(userId);
        resumeGenerator.generatePDF(response,users);
        return "success";
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<String> nwe(@RequestParam("jobId") Long jobId){
        jobService.getUsersWithMatchingSkills(jobId);
        return ResponseEntity.ok("k");
    }
    @DeleteMapping("/delete-job/{jobId}")
    public void deleteJob(@PathVariable Long jobId){
        jobService.deletePostedJob(jobId);
    }
    @PostMapping("/create-profile")
    public ApiResponse makeProfile(Principal principal, @RequestBody UserProfileDto dto, MultipartFile imgFile) throws IOException {
        UserProfile up=usersService.createProfile(dto, principal,imgFile);
        return new ApiResponse("Profile has been set",up,HttpStatus.CREATED);
    }
}
