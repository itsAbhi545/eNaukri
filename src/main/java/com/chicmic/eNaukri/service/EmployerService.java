package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.repo.UserProfileRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private final UsersService usersService;
    private final CompanyService companyService;
    private final UsersRepo usersRepo;
    private final JobRepo jobRepo;
    private final UserProfileRepo userProfileRepo;
    private final EmployerRepo employerRepo;

    public Users saveEmployer(EmployerDto employerDto) {
        Users user = new Users();
        BeanUtils.copyProperties(employerDto, user);

        Employer employer = new Employer();
        BeanUtils.copyProperties(employerDto, employer);

        Company company = new Company();
        BeanUtils.copyProperties(employerDto, company);

        List<Employer> employerList = companyService.findEmployerByEmail(company.getEmail());
        employerList.add(employer);
        company.setEmployerList(employerList);
        employer.setEmployerCompany(company);
        employer.setUsers(user);
        user.setEmployerProfile(employer);
        return usersService.register(user);
    }
    public Users inviteForJob(Long userId, Long jobId, Principal principal){
        Users invitedUser=usersRepo.findById(userId)
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"User not found or deleted"));
        Job job=jobRepo.findById(jobId)
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job not found or deleted"));
        List<Job> jobs=invitedUser.getUserProfile().getInvitedJobs();
        List<UserProfile> invitedUsers=job.getInvitedUsers();
        Users employer=usersService.getUserByEmail(principal.getName());
        List<Job> postedJobs=employer.getEmployerProfile().getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to send invites for this job");
        }
        if(job.isActive()&& postedJobs.contains(job)){
            jobs.add(job);
            invitedUsers.add(invitedUser.getUserProfile());
        }
        return invitedUser;
    }
    public List<Application> getApplicantListForJob(Principal principal,Long jobId){
        Job job=jobRepo.findById(jobId)
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job not found or deleted"));
        Users employer=usersService.getUserByEmail(principal.getName());
        List<Job> postedJobs=employer.getEmployerProfile().getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to see applicants for this job");
        }
        return job.getApplicationList();
    }
//    public List<Users> getUsersByJobRequirements(Principal principal,Long jobId){
//        Job job=jobRepo.findById(jobId)
//                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job not found or deleted"));
//        Users employer=usersService.getUserByEmail(principal.getName());
//        List<Job> postedJobs=employer.getEmployerProfile().getJobList();
//        if(!postedJobs.contains(job)){
//            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to see applicants for this job");
//        }
//        job.getRemoteHybridOnsite();
//
//    }
}
