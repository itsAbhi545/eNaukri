package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Roles;
import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.ApplicationService;
import com.chicmic.eNaukri.service.EmployerService;
import com.chicmic.eNaukri.service.JobService;
import com.chicmic.eNaukri.service.RolesService;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;
    private final JobService jobService;
    private final RolesService rolesService;

    @PostMapping("/signup")
    public ApiResponse signup( String jsonString , @ModelAttribute MultipartFile userImg,
                              @ModelAttribute MultipartFile companyImg) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UsersDto usersDto = mapper.readValue(jsonString, UsersDto.class);
        Employer employer = employerService.saveEmployer(usersDto, userImg, companyImg);
        rolesService.addRoleToUser("EMPLOYER", employer.getUsers());

        return new ApiResponse( "User Register Successfully as Employer", employer, HttpStatus.CREATED );
    }
    @PostMapping("/update")
    public ApiResponse update(String jsonString , @ModelAttribute MultipartFile userImg,
                              @ModelAttribute MultipartFile companyImg, Principal principal) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UsersDto usersDto = mapper.readValue(jsonString, UsersDto.class);
        Employer employer = employerService.updateEmployer(principal,usersDto, userImg, companyImg);
        rolesService.addRoleToUser("EMPLOYER", employer.getUsers());

        return new ApiResponse( "User Register Successfully as Employer", employer, HttpStatus.CREATED );
    }
    @PutMapping("/{appId}/send-invites")
    public ApiResponse sendInvites(Principal principal,Long appId){
        Application app=employerService.inviteForJob(appId,principal);
        return new ApiResponse("User has been successfully invited",app,HttpStatus.OK);}

    @PostMapping("/addRole")
    public String addRole(@RequestParam String roleName){
        Roles roles = rolesService.getRoleByRoleName(roleName);
        if(roles == null){
            roles = Roles.builder()
                  .roleName(roleName.toUpperCase())
                  .build();
            rolesService.saveRoles(roles);
        }
        return "Successfully Added " + roleName;
    }
    @PutMapping("/set-job-status/{jobId}")
    public ResponseEntity<String> setJobStatus(@PathVariable Long jobId, Principal principal, boolean active){
        jobService.setStatus(jobId,active,principal);
        return ResponseEntity.ok("Status changed");
    }
    @PutMapping("/set-application-status/{appId}")
    public ApiResponse setApplicationStatus(@RequestParam Long statusId,@PathVariable Long appId,Principal principal){
        Application app=applicationService.changeApplicationStatus(appId,principal,statusId);
        return new ApiResponse("An email has been sent to applicant regarding application status",app,HttpStatus.ACCEPTED);
    }
    @GetMapping("/applicants/{jobId}")
    public List<Application> getAllApplicants(Principal principal,@PathVariable Long jobId){
        return employerService.getApplicantListForJob(principal,jobId);
    }
    @DeleteMapping("/delete/job/{jobId}")
    public String deleteJob(@PathVariable Long jobId){
        jobService.deleteJob(jobId);
        return "Successfully Deleted Job";
    }
}
