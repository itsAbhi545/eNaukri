package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.CompanyDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
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
    private final CompanyCategoriesRepo companyCategoriesRepo;

    private final CompanyRepo companyRepo;
    private final JobRepo jobRepo;
    private final EmployerRepo employerRepo;
    private final UsersService usersService;
    private final RolesService rolesService;
    private final CategoriesRepo categoriesRepo;

    public Company save(Company company) {
        //company.setApproved(false);
        String uuid = UUID.randomUUID().toString();
        company.setUuid(uuid);
        return companyRepo.save(company);
    }
    public Company companySignup(CompanyDto companyDto){
        Users users = Users.builder()
                .email(companyDto.getEmail())
                .password(companyDto.getPassword())
                .phoneNumber(companyDto.getPhoneNumber()).build();
        users = usersService.register(users);
        companyDto.getCompany().setUsers(users);
        Company company = save(companyDto.getCompany());
        List<Categories> categoriesList=categoriesRepo.findAllById(companyDto.getCategories());
        List<CompanyCategories> companyCategoriesList = new ArrayList<>();
        for(Categories category : categoriesList){
            companyCategoriesList.add(new CompanyCategories(null, company, category));
        }
        companyCategoriesRepo.saveAll(companyCategoriesList);
        //Role
        Roles roles = rolesService.getRoleByRoleName("Company");
        UserRole userRole = UserRole.builder()
                .userId(users)
                .roleId(roles)
                .build();
        rolesService.saveUserRole(userRole);
        return company;
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
    public Set<Employer> findEmployerById(Long id) {
        Company company = companyRepo.findById(id).get();
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

    public List<Company> searchCompany(String query) {
        return companyRepo.findCompanyByQuery(query);
    }
}
