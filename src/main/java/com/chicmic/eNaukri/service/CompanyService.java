package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.shaded.org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepo;
    private final JobRepo jobRepo;
    private final EmployerRepo employerRepo;

    public Company save(Company company) {
        //company.setApproved(false);
        String uuid = UUID.randomUUID().toString();
        company.setUuid(uuid);
        return companyRepo.save(company);
    }
    public Company findByID(Long id) {
        return companyRepo.findById(id).get();
    }
    public Company findByUUID(String uuid) {
        return companyRepo.findByUuid(uuid);
    }
    public void approveCompany(Company company) {
        company.setApproved(true);
        String uuid = UUID.randomUUID().toString();
        company.setUuid(uuid);
        companyRepo.save(company);
    }
    public void disApproveCompany(Company company) {
        company.setApproved(false);
        companyRepo.save(company);
    }

    public Job jobExistsForCompany(Long id, Long jobId) {
        Company company = companyRepo.findById(id).get();
        Job reqjob = jobRepo.findById(jobId).get();
        final boolean[] flag = {false};
        company.getEmployerSet().forEach(employer -> {
           employer.getJobList().forEach(job -> {
               if (jobId.equals(job.getJobId())) flag[0] = true;
           });
        });
        if (flag[0] = true) {
            return reqjob;
        }
        return null;
    }

    public Set<Job> getJobsForCompany(Long id) {
        Company company = companyRepo.findById(id).get();
        Set<Job> jobSet = new HashSet<>();
        company.getEmployerSet().forEach(employer -> {
            employer.getJobList().forEach(job -> {
                jobSet.add(job);
            });
        });
        return jobSet;
    }
    public Set<Employer> findEmployerByEmail(String email) {
        Company company = companyRepo.findByEmail(email);
        return  company == null ? new HashSet<>() : company.getEmployerSet();
    }
    public Company getCompanyByEmail(String email) {
        return companyRepo.findByEmail(email);
    }
    public Company findCompanyById(Long id){
        return companyRepo.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"not found"));
    }
    public Employer approveEmployer(Long empId){
        Employer employer=employerRepo.findById(empId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"employer doesn't exist"));
        if(employer.getIsApproved()==true){
            employer.setIsApproved(false);
        }
        else{
            employer.setIsApproved(true);
        }
        return employer;
    }
    public List<Application> getApplicants(Principal principal,Long jobId){
        Company company = companyRepo.findByEmail(principal.getName());
        Job job=jobRepo.findById(jobId).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND,"already deleted"));
        if(getJobsForCompany(company.getCompanyId()).contains(job)){
            return job.getApplicationList();
        }
        return null;
    }
}
