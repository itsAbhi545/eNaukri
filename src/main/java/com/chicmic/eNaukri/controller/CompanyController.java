package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.Dto.CompanyDto;
import com.chicmic.eNaukri.Dto.JobDto;
import com.chicmic.eNaukri.Dto.SocialLinkDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company/")
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

        Employer employer = employerService.findByUsers(usersService.getUserByEmail(principal.getName()));
        Job job = jobService.saveJob(jobDto, employer);
        return new ApiResponse("Job successfully posted", job, HttpStatus.CREATED);
    }
    //    @PostMapping("add-social-links/{id}")
//    public ResponseEntity<String> addSocialLinks(@PathVariable Long id, SocialLinkDto dto){
//        linkService.addSocialLinks(null, dto,id);
//        return ResponseEntity.ok("Added social links");
//    }
    @DeleteMapping("/delete-job/{jobId}")
    public void deleteJob(@PathVariable Long jobId){
        jobService.deletePostedJob(jobId);
    }
    @GetMapping("list-interested-applicants/{jobId}")
    public Collection<?> listInterestedApplicants(@PathVariable("jobId")Long jobId){
        return jobService.listInterestedApplicants(jobId);
    }
    @PostMapping("approve-employer/{empId}")
    public ApiResponse approve(@PathVariable Long empId){
        return new ApiResponse("Employer status changed",companyService.approveEmployer(empId),HttpStatus.CREATED);
    }
    @PostMapping("applicants-for-job/{jobId}")
    public ApiResponse getapplications(Principal principal, @PathVariable Long jobId){
        return new ApiResponse("Applications for the job",companyService.getApplicants(principal,jobId),HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ApiResponse signUp(@RequestBody CompanyDto companyDto){
        Company company = companyService.companySignup(companyDto);
        System.out.println("Company");
        return new ApiResponse("Company " + company.getUsers().getEmail() + " Register Successfully", company, HttpStatus.CREATED);
    }
//    @PostMapping("/update")
//    public ApiResponse companyUpdate(@RequestBody Company companyReq, Principal principal){
//        Company company = companyService.updateCompany(companyReq, principal);
//
//        return new ApiResponse("Company " + company.getUsers().getEmail() + " Register Successfully", company, HttpStatus.CREATED);
//    }

    //Search
    //filter
    @GetMapping("/search")
    public ApiResponse search(@RequestParam("query") String query,
                              @RequestParam(required = false,name = "location", defaultValue = "") String location,
                              @RequestBody List<Long> categoryIds,
                              Principal principal
    ) {
        if(location.equals("") && principal != null){
            Users user = usersService.getUserByEmail(principal.getName());
            location = usersService.getUserProfile(user).getPreference().getLocation();
        }
        List<Company> companyList = companyService.searchCompany(query, location, categoryIds);
        return new ApiResponse("Search Result Successfully Generated for the query", companyList, HttpStatus.OK);
    }
    @PostMapping("create-profile")
    public ApiResponse createCompanyProfile(Principal principal, SocialLinkDto dto, String key, @RequestParam(required = false) MultipartFile companyImg, String foundedIn) throws IOException {
        Company company=companyService.createCompanyProfile(principal, key,companyImg,dto,foundedIn);

        return new ApiResponse("Created profile",company, HttpStatus.OK);
    }
    @PostMapping("update-profile")
    public ApiResponse updateCompanyProfile(Principal principal,SocialLinkDto dto, Company company, String foundedIn, @RequestParam MultipartFile companyImg,String key) throws IOException {
        return new ApiResponse("details updated",companyService.updateCompany(company,dto,principal,companyImg,key,foundedIn),HttpStatus.OK);
    }
    @GetMapping("login-details")
    public ResponseEntity<?> logincheck(Principal principal){
        return ResponseEntity.ok(companyService.findCompanyByUser(userService.loginResponse(principal)));
    }

}
