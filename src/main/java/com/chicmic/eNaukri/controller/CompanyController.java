package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.*;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.service.*;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {
    private final UserServiceImpl userService;
    private final JobRepo jobRepo;
    private final CompanyRepo companyRepo;
    private final CompanyService companyService;
    private final JobService jobService;
    private final EmployerService employerService;
    private final RolesService rolesService;
    private final SocialLinkService linkService;
    private final UsersService usersService;



    @GetMapping("{id}")
    public Company companyPage(@PathVariable Long id){
        return companyRepo.findById(id).get();
    }
    @GetMapping("{id}/jobs/{jobId}")
    public ResponseEntity<?> getjobFromCompany(@PathVariable("id")Long id, @PathVariable("jobId") Long jobId){
            return ResponseEntity.ok(companyService.jobExistsForCompany(id, jobId));
    }
    @PostMapping("postJob")
    public ApiResponse postJob(@RequestBody JobDto jobDto, Principal principal){

        Employer employer = employerService.findByUsers(userService.getUserByEmail(principal.getName()));
        Job job = jobService.saveJob(jobDto, employer);
        return new ApiResponse("Job successfully posted", job, HttpStatus.CREATED);
    }
    @PutMapping("{id}/setStatus")
    public ResponseEntity<String> setStatus(@PathVariable Long id,boolean active){
        jobService.setStatus(id,active);
        return ResponseEntity.ok("Status changed");
    }
    @PostMapping("{id}/addSocialLinks")
    public ResponseEntity<String> addSocialLinks(@PathVariable Long id, SocialLinkDto dto){
        linkService.addSocialLinks(null, dto,id);
        return ResponseEntity.ok("Added social links");
    }
    @PostMapping("/signup")
    public ApiResponse signUp(@RequestBody CompanyDto companyDto){

        Users users = Users.builder()
                .email(companyDto.getEmail())
                .password(companyDto.getPassword())
                .phoneNumber(companyDto.getPhoneNumber()).build();
        users = usersService.register(users);
        companyDto.getCompany().setUsers(users);
        Company company = companyService.save(companyDto.getCompany());
        //Role
        Roles roles = rolesService.getRoleByRoleName("Company");
        UserRole userRole = UserRole.builder()
                .userId(users)
                .roleId(roles)
                .build();
        rolesService.saveUserRole(userRole);

        return new ApiResponse("Company " + users.getEmail() + " Register Successfully", company, HttpStatus.CREATED);
    }
    //Search
    @GetMapping("/search")
    public ApiResponse search(@RequestParam("query") String query) {
        List<Company> companyList = companyService.searchCompany(query);
        return new ApiResponse("Search Result Successfully Generated for the query", companyList, HttpStatus.OK);
    }


}
