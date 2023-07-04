package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;
    private final PreferenceRepo preferenceRepo;
    private final EmployerRepo employerRepo;
    private final UsersRepo usersRepo;
    private final JobRepo jobRepo;

    private final ApplicationRepo applicationRepo;

    public Double checkCompletionStatus(Object obj) throws IllegalAccessException {
        Long nullFields = 0L;
        Long total = 0L;
        for (Field field : obj.getClass().getDeclaredFields()){
            if((field.getType() == String.class || field.getType() == Long.class ) && !field.getName().equals("id")) {
                field.setAccessible(true);
                System.out.println("\u001B[35m" + field.getName() + ": " + field.get(obj) + "\u001B[0m");
                if (field.get(obj) == null || field.get(obj).equals("")) {
                    nullFields++;
                }
                total++;
            }
        }
        System.out.println("\u001B[35m" + nullFields + " : " + total + "\u001B[0m");


        return (double) (((total - nullFields) * 100)/total);
    }


    public Employer saveEmployer(UsersDto usersDto, MultipartFile userImg) throws IOException, IllegalAccessException {
        Employer employer = usersDto.getEmployerProfile();
        employer.setIsApproved(false);

        employer.setPpPath(FileUploadUtil.imageUpload(userImg));
        if (usersDto.getCompanyId() != null) {
            Company company = companyService.findByID(usersDto.getCompanyId());
            Set<Employer> employerSet = companyService.findEmployerById(usersDto.getCompanyId());
            employerSet.add(employer);
            company.setEmployerSet(employerSet);
            employer.setCompany(company);
        }
        else {employer.setCompany(null);}

        Users users = new Users();
        BeanUtils.copyProperties(usersDto, users);
        employer.setUsers(users);
        usersService.register(users);
        employer = employerRepo.save(employer);
        employer.setCompletionStatus(checkCompletionStatus(employer));
        employer = employerRepo.save(employer);
        return employer;
    }
    public Employer updateEmployer(Principal principal,UsersDto usersDto, MultipartFile userImg, MultipartFile companyImg) throws IOException {
        Employer employer = findByUsers(usersService.getUserByEmail(principal.getName()));
        if(employer == null || !employer.equals(usersDto.getEmployerProfile())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Employer does not exist");
        }
        Company company = companyService.findByID(usersDto.getCompanyId());
        if(!company.equals(employer.getCompany())){
            employer.setIsApproved(false);
            Set<Employer> employerSet = companyService.findEmployerById(usersDto.getCompanyId());
            employerSet.add(employer);
            company.setEmployerSet(employerSet);
            employer.setCompany(company);
            company.setPpPath(FileUploadUtil.imageUpload(companyImg));
        }
        employer.setPpPath(FileUploadUtil.imageUpload(userImg));
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
        List<Job> jobs=usersService.getUserProfile(invitedUser).getInvitedJobs();
        List<UserProfile> invitedUsers=job.getInvitedUsers();
        Users employer=usersService.getUserByEmail(principal.getName());
        Set<Job> postedJobs=findByUsers(employer).getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to send invites for this job");
        }
        if(job.isActive()&& postedJobs.contains(job)){
            jobs.add(job);
            invitedUsers.add(usersService.getUserProfile(invitedUser));
            String to=invitedUser.getEmail();
            String subject="Invitation to apply for a job opening at "+ findByUsers(employer).getCompany().getName();
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
        Set<Job> postedJobs=findByUsers(employer).getJobList();
        if(!postedJobs.contains(job)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Not allowed to see applicants for this job");
        }
        return job.getApplicationList();
    }

    //Search

//    public Set<Users> searchUsersForJob(Job job, Long[] skills){
//        Arrays.sort(skills);
//        List<UserProfile> userProfileList = preferenceRepo.searchUserPreferencesByJob(job);
//        Set<Users> usersSet = new HashSet<>();
//
//        userProfileList.forEach(userProfile -> {
//            userProfile.getUserSkillsList().forEach(userSkill -> {
//                if(Arrays.binarySearch(skills, userSkill.getUserSkillId()) >= 0){
//                    usersSet.add(userProfile.getUsers());
//                }
//            });
//        });
//        return usersSet;
//    }
//    public List<Employer> searchEmployers(String query) {
//        return employerRepo.findEmployersByAnyMatch(query);
//    }




    public Employer findByUsers(Users users){
        return employerRepo.findByUsers(users);
    }


}
