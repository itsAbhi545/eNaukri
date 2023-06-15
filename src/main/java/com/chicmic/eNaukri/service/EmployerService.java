package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;




    public Users saveEmployer(Users users, MultipartFile userImg,MultipartFile companyImg) throws IOException {
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
