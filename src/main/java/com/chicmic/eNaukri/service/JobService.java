package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.JobDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final JobRepo jobRepo;
    private final UsersRepo usersRepo;
    private final CompanyRepo companyRepo;
    private final JobSkillsRepo jobSkillsRepo;
    private final UserSkillsRepo userSkillsRepo;
    private final SkillsRepo skillsRepo;
    private final UserCompanyRepo userCompanyRepo;
    @PersistenceContext
    private EntityManager entityManager;
    UsersService usersService;
    @Async public void saveJob(JobDto job, Long companyId) {
        String postedFor=companyRepo.findById(companyId).get().getCompanyName();
        Users user=usersRepo.findById(job.getUserId()).get();
        ObjectMapper mapper = CustomObjectMapper.createObjectMapper();
        Job newJob = mapper.convertValue(job, Job.class);
        newJob.setActive(true);
        newJob.setPostedOn(LocalDate.now());
        System.out.println("\u001B[33m"+user.getCurrentCompany()+"\u001B[0m");
        if(user.getCurrentCompany().equals(postedFor)){
            System.out.println("inside if"+companyRepo.findByCompanyName(postedFor.trim()).getCompanyName());
            newJob.setPostFor(companyRepo.findByCompanyName(postedFor.trim()));
        }
//        else if(user.getCurrentCompany()==null){
//            newJob.setPostFor();
//        }
        List<JobSkills> newJobSkillList = new ArrayList<>();

        for (String jobSkillId : job.getSkillsList()) {
            Long skillId = Long.valueOf(jobSkillId);
//            System.out.println("inside service job"+skillId);
            Skills skill=skillsRepo.findById(skillId).orElse(null);
            JobSkills jobSkill = JobSkills.builder().jobSkill(skill).job(newJob).build();
            if (skill != null) {
                newJobSkillList.add(jobSkill);
            }
        }
        System.out.println(newJobSkillList.get(0)+"job skill selected");
        newJob.setJobSkillsList(newJobSkillList);
        jobRepo.save(newJob);
        List<Users> usersList=getUsersWithMatchingSkills(newJob.getJobId());
        sendEmailNotifications(usersList,newJob);
    }
    public List<Job> displayFilteredPaginatedJobs(String query, String location, String jobType, String postedOn, String remoteHybridOnsite) {
        CriteriaBuilder builder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> criteriaQuery=builder.createQuery(Job.class);
        Root<Job> root=criteriaQuery.from(Job.class);
        Predicate queryInTitle=(!StringUtils.isEmpty(query))?builder.like(root.get("jobTitle"),"%"+ query +"%"):builder.like(root.get("jobTitle"),"%%");
        Predicate queryInDesc=(!StringUtils.isEmpty(query))?builder.like(root.get("jobDesc"),"%"+ query +"%"):builder.like(root.get("jobTitle"),"%%");
        Predicate locationQuery=(!StringUtils.isEmpty(location))?builder.like(root.get("location"),"%"+ location +"%"):builder.like(root.get("location"),"%%");
        Predicate jobTypeQuery=(!StringUtils.isEmpty(jobType))?builder.equal(root.get("jobType"),jobType):builder.like(root.get("jobType"),"%%");

        Predicate postedOnQuery=builder.like(root.get("jobType"),"%%");
        if(!StringUtils.isEmpty(postedOn)){
            LocalDate currenttime=LocalDate.now();

            switch (postedOn){
                case "24hours":
                    postedOnQuery=builder.greaterThanOrEqualTo(root.get("postedOn"),currenttime.minusDays(1));break;
                case "thisWeek":
                    postedOnQuery=builder.greaterThanOrEqualTo(root.get("postedOn"),currenttime.minusWeeks(1));break;
                case "thisMonth":
                    postedOnQuery=builder.greaterThanOrEqualTo(root.get("postedOn"),currenttime.minusMonths(1));break;
                default:
                    postedOnQuery=builder.like(root.get("jobType"),"%%");break;
            }
        }
        Predicate workTypeQuery=(!StringUtils.isEmpty(remoteHybridOnsite))?builder.equal(root.get("remoteHybridOnsite"),remoteHybridOnsite):builder.like(root.get("remoteHybridOnsite"),"%%");
        Predicate activeJobs=builder.isTrue(root.get("active"));

        //building query
        criteriaQuery.where(builder.or(queryInTitle,queryInDesc),locationQuery,jobTypeQuery,postedOnQuery,workTypeQuery,activeJobs);
        //typedQuery for future purposes
        TypedQuery<Job> jobTypedQuery=entityManager.createQuery(criteriaQuery);
        return jobTypedQuery.getResultList();
    }

    public Collection<?> listInterestedApplicants(Long jobId) {
        Job job=jobRepo.findById(jobId).get();

        List<JobSkills> jobSkillsList = job.getJobSkillsList();
        Collection<UserSkills> usersSet=new HashSet<>();

        jobSkillsList.forEach(jobSkills ->{
            usersSet.addAll(userSkillsRepo.findBySkills(jobSkills.getJobSkill()));
        } );
        Collection<String> usersCollection=new HashSet<>();
        usersSet.forEach(userSkills ->{
            if(userSkills.getUser().isEnableNotification())usersCollection.add(userSkills.getUser().getEmail());
        } );
        return usersCollection;
    }
    public void setStatus(Long jobId, boolean active) {
        Optional<Job> job = jobRepo.findById(jobId);
        if (job.isPresent()) {
            Job job1 = job.get();
            job1.setActive(active);
            jobRepo.save(job1);
        }
    }

    public List<Users> getUsersWithMatchingSkills(Long jobId) {
        Job job=jobRepo.findById(jobId).get();
        List<JobSkills> requiredSkills=job.getJobSkillsList();
        Set<UserSkills> userSet=new HashSet<>();
        requiredSkills.forEach(jobSkillss ->{
                  userSet.addAll(userSkillsRepo.findBySkills(jobSkillss.getJobSkill()));
        });
        List<Users> usersSet=new ArrayList<>();
        userSet.forEach(userSkills -> usersSet.add(userSkills.getUser()));
        System.out.println(usersSet);
        return usersSet;
    }
    private void sendEmailNotifications(List<Users> users, Job job) {
        for (Users users1 : users) {
                String body = "Dear " + users1.getFullName() + ",\n"
                        + "A new job matching your skills has been posted.\n"
                        + "Job Title: " + job.getJobTitle() + "\n"
                        + "Job Description: " + job.getJobDesc() + "\n"
                        + "Please consider applying if you are interested.\n"
                        + "Best regards,\n"
                        + "Your Job Portal Team";
                // Send email to the user
                // ...
                String to = users1.getEmail();
                String subject = "Matching job";
            if (users1.isEnableNotification()) {
                usersService.sendEmailForOtp(to, subject, body);
            }
        }
    }
}
