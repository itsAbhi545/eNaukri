package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.Dto.CompanyDto;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;


@RestController
//@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final UsersRepo usersRepo;
    private final UsersService usersService;
    private final UserServiceImpl userService;
    private final JobService jobService;
    private final PasswordResetService passwordResetService;
    private final ResumeGenerator resumeGenerator;
    private final CompanyService companyService;
    private final RolesService rolesService;


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
        return new ApiResponse("user created",user,HttpStatus.CREATED );
    }
    @PostMapping("/updateProfile")
    public void updateProfile(UsersDto user, @RequestParam(value="resumeFile",required = false)MultipartFile resumeFile, @RequestParam(value="imgFile",required = false)MultipartFile imgFile) throws IOException {
        usersService.updateUser(user,imgFile,resumeFile);
    }
    @GetMapping("logout-user")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        userService.logout(request,response);
        return "Logout Successful";
    }
    @GetMapping("forgot-password")
    public String forgotPassword(){
        return "forgot password?";
    }
    @PostMapping("forgot-password")
    public boolean sendForgotPaswdLink(@RequestParam String email){
        return true;
    }



    @GetMapping("{jobId}/listInterestedApplicants")
    public Collection<?> listInterestedApplicants(@PathVariable("jobId")Long jobId){
        return jobService.listInterestedApplicants(jobId);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<String> setPassword(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        Users user = userService.getUserByEmail(email);
        //passwordResetService.createPasswordResetTokenForUser(user);
        return ResponseEntity.ok("Mail sent");
    }
    @GetMapping("/enterNewPassword/{token}/{uuid}")
    public String Enter(HttpServletRequest
                                request, @PathVariable("token") String token, @PathVariable("uuid") String uuid, Model model) {
        PasswordResetToken passwordResetRequest = passwordResetService.findByToken(token);
        if (passwordResetRequest == null || passwordResetRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetService.delete(passwordResetRequest);
            return "redirect:/login?error=InvalidToken";
        }
        model.addAttribute("token", token);
        return "forgotPasswordForm";
    }
    @GetMapping("/{userId}/download-pdf")
    public String downloadPDF(HttpServletResponse response, @PathVariable Long userId) throws IOException, DocumentException {
        Users users=usersService.getUserById(userId);
        resumeGenerator.generatePDF(response,users);
        return "success";
    }
    @PostMapping("/enterNewPassword/{token}/{uuid}")
    public String resetPassword(HttpServletRequest
                                        request,@PathVariable("token") String token,@PathVariable("uuid") String uuid){
        PasswordResetToken passwordResetRequest = passwordResetService.findByToken(token);
        Users user = userService.getUserByUuid(uuid);
        System.out.println(user);
        String newPassword=request.getParameter("password");
        if (passwordResetRequest == null || passwordResetRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetService.delete(passwordResetRequest);
            return "redirect:/login?error=InvalidToken";
        }
        user.setPassword(passwordEncoder().encode(newPassword));
        usersRepo.save(user);
        passwordResetService.delete(passwordResetRequest);
        return "user-login";
    }
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<String> nwe(@RequestParam("jobId") Long jobId){
        jobService.getUsersWithMatchingSkills(jobId);
        return ResponseEntity.ok("k");
    }



}
