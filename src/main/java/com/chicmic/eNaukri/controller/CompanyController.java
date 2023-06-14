package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.JobDto;
import com.chicmic.eNaukri.Dto.SocialLinkDto;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.SocialLink;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company/")
@RequiredArgsConstructor
public class CompanyController {
    private final UserServiceImpl userService;
    private final JobRepo jobRepo;
    private final CompanyRepo companyRepo;
    private final CompanyService companyService;
    private final ApplicationService applicationService;
    @Autowired JobService jobService;
    SocialLinkService linkService;

    @GetMapping("{id}")
    public Company companyPage(@PathVariable Long id){
        return companyRepo.findById(id).get();
    }
    @GetMapping("{id}/jobs/{jobId}")
    public ResponseEntity<?> getjobFromCompany(@PathVariable("id")Long id, @PathVariable("jobId") Long jobId){
            return ResponseEntity.ok(companyService.jobExistsForCompany(id, jobId));
    }
    @PostMapping("{companyId}/{empId}/postJob")
    public ResponseEntity<String> postJob(@RequestBody JobDto job, @PathVariable Long companyId, @PathVariable Long empId){
        jobService.saveJob(job, companyId, empId);
        return ResponseEntity.ok("Job successfully posted");
    }
    @PutMapping("{empId}/{jobId}/setStatus")
    public ResponseEntity<String> setJobStatus(@PathVariable Long jobId,@PathVariable Long empId, boolean active){
        jobService.setStatus(jobId,active,empId);
        return ResponseEntity.ok("Status changed");
    }
    @PostMapping("{id}/addSocialLinks")
    public ResponseEntity<String> addSocialLinks(@PathVariable Long id, SocialLinkDto dto){
        linkService.addSocialLinks(null, dto,id);
        return ResponseEntity.ok("Added social links");
    }
}
