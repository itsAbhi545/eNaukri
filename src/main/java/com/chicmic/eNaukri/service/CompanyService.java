package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.ExperienceRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepo;
    private final JobRepo jobRepo;

    public Job jobExistsForCompany(Long id, Long jobId) {
        Company company = companyRepo.findById(id).get();
        Job reqjob = jobRepo.findById(jobId).get();
        final boolean[] flag = {false};
        company.getJobList().forEach(job -> {
            if (jobId.equals(job.getJobId())) flag[0] = true;
        });
        if (flag[0] = true) {
            return reqjob;
        }
        return null;
    }

    public List<Job> getJobsForCompany(Long id) {
        Company company = companyRepo.findById(id).get();
        return company.getJobList();
    }
    public List<Employer> findEmployerByEmail(String email) {
        Company company = companyRepo.findByEmail(email);
        return  company == null ? new ArrayList<>() : company.getEmployerList();
    }
}
