package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.service.*;
import com.chicmic.eNaukri.validation.RegEx;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.regex.Pattern;


@RestController
//@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    //abhijeet
    private final CompanyService companyService;
    private final UsersService usersService;
    private final UserServiceImpl userService;
    private final JobService jobService;
    private final PasswordResetService passwordResetService;
    private final ResumeGenerator resumeGenerator;
    private final SkillsService skillsService;
    private final RolesService rolesService;
    private final CategoriesService categoriesService;


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
    @PostMapping("/api/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse register(@Valid Users dto) {
        Users user=usersService.register(dto);
        rolesService.addRoleToUser("USER", user);
        return new ApiResponse("user created",user,HttpStatus.CREATED );
    }

    @PostMapping("send-verification")
    public ApiResponse sendVerificationLink(@RequestParam String email){
        usersService.sendVerificationLink(email);
        return new ApiResponse("verification",null,HttpStatus.OK);
    }
    @GetMapping("eNaukri/verify/{token}/{uuid}")
    public ResponseEntity<String> verifyUser(@PathVariable String token,@PathVariable String uuid){
        usersService.verifyUserAccount(token,uuid);
        return ResponseEntity.ok("User verified successfully");
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

//    @GetMapping("jobs")
//    public List<Job> displayJobs(@RequestParam(required = false,name = "q") String query,
//                                 @RequestParam(required = false,name = "location") String location,
//                                 @RequestParam(required = false,name = "type") String jobType,
//                                 @RequestParam(required = false,name = "postedOn") String postedOn,
//                                 @RequestParam(required = false,name = "remoteHybridOnsite") String remoteHybridOnsite,
//                                 @RequestParam(required = false,name = "yoe") Integer yoe,
//                                 @RequestParam(required = false,name = "salary") Integer salary,
//                                 @RequestParam(required = false,name = "skills") List<Long> skillIds){
//        return jobService.displayFilteredPaginatedJobs(query,location,jobType,postedOn,remoteHybridOnsite,yoe,salary,skillIds);
//    }

    @PostMapping("password-reset-request")
    public ApiResponse sendPasswordResetOtp(@RequestParam String email) throws
            MessagingException, UnsupportedEncodingException {
        String otp=passwordResetService.sendEmailForPasswordReset(email);
        return new ApiResponse("Here's your otp",otp,HttpStatus.OK);
    }
    @PostMapping("verify-otp")
    public ApiResponse verifyOtp(@RequestParam String otp,@RequestParam String email){
        String uuid=passwordResetService.verifyOtp(otp, email);
        return new ApiResponse("Otp verified", uuid,HttpStatus.OK);
    }
    @PostMapping("reset-password")
    public ApiResponse setNewPassword(@RequestParam String token,@RequestParam String newPassword,@RequestParam String confirmPassword){
        Users user=passwordResetService.resetPassword(token, newPassword,confirmPassword);
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
    @GetMapping("jobs-by-company/{companyId}")
    public ApiResponse getJobsByCompany(@PathVariable Long companyId){
        return new ApiResponse("List of Jobs",companyService.getJobsForCompany(companyId),HttpStatus.OK);
    }
    @GetMapping("job-categories")
    public List<Categories> showCategories(){
        return jobService.showJobCategories();
    }
    @GetMapping("search-job-categories-by-name")
    public ApiResponse getCategoriesByName(@RequestParam String query){
        return new ApiResponse("Job Categories according to keyword",categoriesService.searchCategories(query),HttpStatus.OK);
    }
    @GetMapping("check-if-user-exists")
    public void checkIfUserExists(@RequestParam String email){
        Users user=usersService.getUserByEmail(email);
        if(user!=null){
            throw new ApiException(HttpStatus.FORBIDDEN,"User already exists");
        }
        if(user==null&&Pattern.matches(RegEx.EMAIL,email)){
            throw new ApiException(HttpStatus.ACCEPTED,"No user by this email proceed further");
        }
        if(!Pattern.matches(RegEx.EMAIL,email)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Invalid mail address");
        }
    }


}
