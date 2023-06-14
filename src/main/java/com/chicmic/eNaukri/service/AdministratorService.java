package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.ApplicationStatusRepo;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.repo.JobCategoriesRepo;
import com.chicmic.eNaukri.repo.SkillsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service@RequiredArgsConstructor
public class AdministratorService {
    private final EmployerRepo employerRepo;
    private final ApplicationStatusRepo applicationStatusRepo;
    private final JobCategoriesRepo jobCategoriesRepo;
    private final SkillsRepo skillsRepo;
    public ApplicationStatus createApplicationStatus(ApplicationStatus dto){
        ApplicationStatus applicationStatus=applicationStatusRepo.save(dto);
        return applicationStatus;
    }
    public void approveEmployer(Long empId,boolean approve){
        Employer employer=employerRepo.findById(empId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"employer doesn't exist"));
        employer.setApproved(approve);
    }
    public JobCategories createJobCategories(JobCategories dto){
        JobCategories jobCategories=jobCategoriesRepo.save(dto);
        return jobCategories;
    }
    public Skills createSkills(Skills dto){
        if(skillsRepo.existsBySkillName(dto.getSkillName())){
            throw new ApiException(HttpStatus.CONFLICT,"A skill by the same skill name exists");
        }
        Skills newSkill=skillsRepo.save(dto);
        return newSkill;
    }
//    public List<Users> allUsers(){
//
//    }
}
