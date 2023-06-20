package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.repo.PreferenceRepo;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;
    private final PreferenceRepo preferenceRepo;
    private final EmployerRepo employerRepo;

    private final PreferenceRepo preferenceRepo;
    private final EmployerRepo employerRepo;
    private final UsersRepo usersRepo;
    private final JobRepo jobRepo;
    private final ApplicationRepo applicationRepo;

    public Employer saveEmployer(UsersDto usersDto, MultipartFile userImg, MultipartFile companyImg) throws IOException {
        Employer employer = usersDto.getEmployerProfile();
        employer.setIsApproved(false);
        Company company = companyService.findByID(usersDto.getCompanyId());
        employer.setPpPath(FileUploadUtil.imageUpload(userImg));
        company.setPpPath(FileUploadUtil.imageUpload(companyImg));

        Set<Employer> employerSet = companyService.findEmployerById(usersDto.getCompanyId());
        employerSet.add(employer);
        company.setEmployerSet(employerSet);
        employer.setCompany(company);
        Users users = new Users();
        BeanUtils.copyProperties(usersDto, users);
        employer.setUsers(users);
        usersService.register(users);
        return employerRepo.save(employer);
    }

    //Search

    public Set<Users> searchUsersForJob(Job job, Long[] skills){
        Arrays.sort(skills);
        List<UserProfile> userProfileList = preferenceRepo.searchUserPreferencesByJob(job);
        Set<Users> usersSet = new HashSet<>();

        userProfileList.forEach(userProfile -> {
            userProfile.getUserSkillsList().forEach(userSkill -> {
                if(Arrays.binarySearch(skills, userSkill.getUserSkillId()) >= 0){
                    usersSet.add(userProfile.getUsers());
                }
            });

        });
        return usersSet;
    }
    public List<Employer> searchEmployers(String query) {
        return employerRepo.findEmployersByAnyMatch(query);
    }
    public Application inviteForJob(Long appId, Principal principal){
        Application application=applicationRepo.findById(appId).get();
        Users invitedUser=application.getApplicantId().getUsers();
        Job job=jobRepo.findById(application.getJobId().getJobId())
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job not found or deleted"));
        List<Job> jobs=invitedUser.getUserProfile().getInvitedJobs();
        List<UserProfile> invitedUsers=job.getInvitedUsers();
        Users employer=usersService.getUserByEmail(principal.getName());
        Set<Job> postedJobs=employer.getEmployerProfile().getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to send invites for this job");
        }
        if(job.isActive()&& postedJobs.contains(job)){
            jobs.add(job);
            invitedUsers.add(invitedUser.getUserProfile());
            String to=invitedUser.getEmail();
            String subject="Invitation to apply for a job opening at "+ employer.getEmployerProfile().getCompany().getName();
            String body = "You have been invited to apply for the job at"+ job.getEmployer().getCompany().getName()+" for " +
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
        Set<Job> postedJobs=employer.getEmployerProfile().getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to see applicants for this job");
        }
        return job.getApplicationList();
    }

    //Search

    public Set<Users> searchUsersForJob(Job job, Long[] skills){
        Arrays.sort(skills);
        List<UserProfile> userProfileList = preferenceRepo.searchUserPreferencesByJob(job);
        Set<Users> usersSet = new HashSet<>();

        userProfileList.forEach(userProfile -> {
            userProfile.getUserSkillsList().forEach(userSkill -> {
                if(Arrays.binarySearch(skills, userSkill.getUserSkillId()) >= 0){
                    usersSet.add(userProfile.getUsers());
                }
            });
        });
        return usersSet;
    }
    public List<Employer> searchEmployers(String query) {
        return employerRepo.findEmployersByAnyMatch(query);
    }




    public Employer findByUsers(Users users){
        return employerRepo.findByUsers(users);
    }


}
