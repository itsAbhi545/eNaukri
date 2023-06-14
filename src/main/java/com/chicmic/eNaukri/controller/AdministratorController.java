package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.model.ApplicationStatus;
import com.chicmic.eNaukri.model.JobCategories;
import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.service.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;
    private final EmployerRepo employerRepo;
    @PostMapping("createApplicationStatus")
    public ApiResponse createApplicationStatus(ApplicationStatus applicationStatus){
         applicationStatus=administratorService.createApplicationStatus(applicationStatus);
        return new ApiResponse("Created new status",applicationStatus,HttpStatus.CREATED);
    }
    @PostMapping("approve-company")
    public ApiResponse approve(Long empId,boolean approve){
        administratorService.approveEmployer(empId,approve);
        return new ApiResponse("Employer status changed",employerRepo.findById(empId).get(),HttpStatus.CREATED);
    }
    @PostMapping("create-job-categories")
    public ApiResponse createCategories(JobCategories jobCategories){
        jobCategories=administratorService.createJobCategories(jobCategories);
        return new ApiResponse("created new category",jobCategories,HttpStatus.CREATED);
    }
    @PostMapping("create-skills")
    public ApiResponse addNewSkills(Skills skills){
        skills=administratorService.createSkills(skills);
        return new ApiResponse("skill created",skills,HttpStatus.CREATED);
    }
}
