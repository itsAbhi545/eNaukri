package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepo;
    private final JobRepo jobRepo;

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
    public void deletedCompany(Long id) {
        companyRepo.deleteById(id);
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
    public Set<Employer> findEmployerById(Long id) {
        Company company = companyRepo.findById(id).get();
        return  company == null ? new HashSet<>() : company.getEmployerSet();
    }
    public Company getCompanyByEmail(String email) {
        return companyRepo.findByEmail(email);
    }

    public List<Company> searchCompany(String query) {
        return companyRepo.findCompanyByQuery(query);
    }
}
