package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.ApplicationRepo;
import com.chicmic.eNaukri.repo.JobRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;
    private final UsersRepo usersRepo;
    private final JobRepo jobRepo;
    private final ApplicationRepo applicationRepo;


    public Users saveEmployer(Users users, MultipartFile userImg, MultipartFile companyImg) throws IOException {
        Employer employer = users.getEmployerProfile();
        Company company = users.getEmployerProfile().getEmployerCompany();
        employer.setPpPath( FileUploadUtil.imageUpload(userImg));
        company.setPpPath(FileUploadUtil.imageUpload(companyImg));
        Set<Employer> employerSet = companyService.findEmployerByEmail(company.getEmail());
        employerSet.add(employer);
        company.setEmployerSet(employerSet);
        employer.setEmployerCompany(company);
        employer.setUsers(users);
        users.setEmployerProfile(employer);
        return usersService.register(users);

    }
    public Application inviteForJob(Long appId, Principal principal){
        Application application=applicationRepo.findById(appId).get();
        Users invitedUser=application.getApplicantId().getUsers();
        Job job=jobRepo.findById(application.getJobId().getJobId())
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job not found or deleted"));
        List<Job> jobs=invitedUser.getUserProfile().getInvitedJobs();
        List<UserProfile> invitedUsers=job.getInvitedUsers();
        Users employer=usersService.getUserByEmail(principal.getName());
        Set<Job> postedJobs=employer.getEmployerProfile().getEmployerCompany().getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to send invites for this job");
        }
        if(job.isActive()&& postedJobs.contains(job)){
            jobs.add(job);
            invitedUsers.add(invitedUser.getUserProfile());
            String to=invitedUser.getEmail();
            String subject="Invitation to apply for a job opening at "+ employer.getEmployerProfile().getEmployerCompany().getName();
            String body = "You have been invited to apply for the job at"+ job.getEmployer().getEmployerCompany().getName()+" for " +
                    "the role of "+job.getJobTitle();
            usersService.sendEmail(to,subject,body);
            return application;
        }
        return  null;
    }
    public List<Application> getApplicantListForJob(Principal principal, Long jobId){
        Job job=jobRepo.findById(jobId)
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job not found or deleted"));
        Users employer=usersService.getUserByEmail(principal.getName());
        Set<Job> postedJobs=employer.getEmployerProfile().getEmployerCompany().getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to see applicants for this job");
        }
        return job.getApplicationList();
    }
}
