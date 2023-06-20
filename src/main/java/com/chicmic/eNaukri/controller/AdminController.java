package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.model.UserProfile;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.AdminService;
import com.chicmic.eNaukri.service.CompanyService;
import com.chicmic.eNaukri.service.EmployerService;
import com.chicmic.eNaukri.service.UsersService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final EmployerService employerService;
    private final UsersService usersService;
    private final CompanyService companyService;
    @PostMapping("/addSkills")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse addSkills(@RequestBody Skills skills)  {
        skills = adminService.createSkills(skills);
        return new ApiResponse("Skills added successfully",skills, HttpStatus.CREATED);
    }

    //Search
    @GetMapping("/search/employers")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse searchEmployers(@RequestParam(value = "query") String query) {
        List<Employer> employerList = employerService.searchEmployers(query);
        return new ApiResponse("Generated Employers List",employerList, HttpStatus.OK);
    }@GetMapping("/search/users")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse searchUsers(@RequestParam(value = "query") String query) {
        List<UserProfile> userProfileList = usersService.searchUser(query);
        return new ApiResponse("Generated Users List",userProfileList, HttpStatus.OK);
    }

    //Update
    @PostMapping("/update-profile")
    public ResponseEntity<String> updateAll(@RequestBody UserProfile users,@RequestBody List<Skills> skillsList) {
        //usersService.updateUser(user,imgFile,resumeFile);
        System.out.println("fdf");
        return ResponseEntity.ok("updated successfully");
    }
    //Company
    @GetMapping("/company/{id}/approve")
    public ApiResponse approval(@PathVariable Long id){
        companyService.approveCompany(companyService.findByID(id));
        return new ApiResponse("Company Approved Successfully",null, HttpStatus.OK);
    }
    @GetMapping("/company/{id}/disApprove")
    public ApiResponse disApproval(@PathVariable Long id){
        companyService.disApproveCompany(companyService.findByID(id));
        return new ApiResponse("Company DisApproved Successfully",null, HttpStatus.OK);
    }

}
