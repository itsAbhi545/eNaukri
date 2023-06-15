package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.ApplicationDto;
import com.chicmic.eNaukri.Dto.Status;
import com.chicmic.eNaukri.model.*;

import com.chicmic.eNaukri.repo.*;
import com.chicmic.eNaukri.util.FileUploadUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final FileUploadUtil fileUploadUtil;
    private final ApplicationRepo applicationRepo;
    private final UsersRepo usersRepo;
    private final JobRepo jobRepo;
    private final JavaMailSender javaMailSender;
    private final EmployerRepo employerRepo;
    private final ApplicationStatusRepo statusRepo;
    private final UsersService usersService;
    public void applyForJob(ApplicationDto application, Long userId, Long jobId)
            throws IOException, MessagingException, ApiException {

        Users user = usersRepo.findByUserId(userId);
        Job job = jobRepo.findJobByJobId(jobId);
        MultipartFile resumeFile = application.getResumeFile();

        boolean hasExistingApplication = applicationRepo.existsByApplicantIdAndJobId(user, job);
        if (hasExistingApplication) {
            throw new ApiException(HttpStatus.valueOf(409),"User has already applied to this job.");
        }
        else {
            if (job.isActive() == true) {
                Application jobApplication = new Application();
                BeanUtils.copyProperties(application, jobApplication);
                jobApplication.setCvPath(fileUploadUtil.resumeUpload(resumeFile));
//                jobApplication.setApplicantId(user);
                jobApplication.setJobId(job);
                applicationRepo.save(jobApplication);
                job.setNumApplicants(job.getNumApplicants() + 1);
                jobRepo.save(job);
                String jobTitle = job.getJobTitle();
                String company = job.getPostFor().getName();
                sendEmailOnApplication(user.getEmail(), jobTitle, company);
            }
        }
    }
    @Async
    public void sendEmailOnApplication(String recipientEmail, String jobTitle,String company)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("bluflame.business@gmail.com", "eNaukri Site");
        helper.setTo(recipientEmail);
        String subject = "Your application has been submitted";
        String content = "<p>Hello,</p>"
                + "<p>Your application to the"+ company +"for" +jobTitle+ "has been submitted.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
        System.out.println("email sent");
    }
    public List<Application> viewApplications(Long userId){
        Users user = usersRepo.findById(userId).get();
        List<Application> applications=user.getUserProfile().getApplicationList();
        return applications;
    }
    public int getNumApplicantsForJob(Long jobId) {
        Job job = jobRepo.findById(jobId).orElse(null);
        if(job==null)
            throw new ApiException(HttpStatus.CONFLICT,"not found");
        return job.getNumApplicants();
    }
    public void changeStatus(Long appId, Long empId,Long statusId){
        Application application= applicationRepo.findById(appId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"no such application exists or has been deleted"));
        Employer employer= employerRepo.findById(empId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"no such employer exists"));
        Job job = jobRepo.findJobByJobId(application.getJobId().getJobId());
        ApplicationStatus status= statusRepo.findById(statusId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"no such application status exists"));
        if(job.getEmployer().equals(employer)){
            application.setApplicationStatus(status);
            String to=application.getEmail();
            String subject="Regarding status of your application for "+job.getJobTitle();
            String body="Your application for "+job.getJobTitle()+",at "+job.getPostFor().getCompanyName()+
                    " has been "+status.getMessage();
            usersService.sendEmail(to,subject,body);
        }
    }
}
