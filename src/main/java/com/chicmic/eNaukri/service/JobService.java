package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.JobDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
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
    private final EmployerRepo employerRepo;
    private final EmployerService employerService;
    private final CategoriesRepo categoriesRepo;

    @PersistenceContext
    private EntityManager entityManager;
    UsersService usersService;

    public Job getJobById(Long jobId) {
        return jobRepo.findById(jobId).orElse(null);
    }
    public Job saveJob(JobDto job,Employer employer) {
        String postedFor= employer.getCompany().getName();
        Job newJob = CustomObjectMapper.convertDtoToObject(job,Job.class);
        newJob.setActive(true);
        newJob.setEmployer(employer);
        List<JobSkills> newJobSkillList = new ArrayList<>();
        List<Categories> categoriesList =new ArrayList<>();
        if(job.getJobCategories() != null){
            for (Long jc : job.getJobCategories()) {
                Categories categories = categoriesRepo.findById(jc).get();
                categoriesList.add(categories);
            }
        }
//        else {
        job.getSkillsList().addAll(job.getOtherSkills());
        for (String jobSkillId : job.getSkillsList()) {
            String st = jobSkillId.replaceAll("[^A-Za-z]", "");
            Skills skills = Skills.builder().build();
            if (st.equals("")) {
                Long skillId = Long.valueOf(jobSkillId);

                log.info("value = " + skillId);
                skills = skillsRepo.findById(skillId).orElse(null);

            } else {
                skills = Skills.builder()
                        .skillName(st)
                        .build();
                skills = skillsRepo.save(skills);
            }
            JobSkills jobSkill = JobSkills.builder().skill(skills).job(newJob).build();
            if (skills != null) {
                newJobSkillList.add(jobSkill);
            }
        }
        newJob.setCategories(categoriesList);
        newJob.setJobSkillsList(newJobSkillList);
        newJob = jobRepo.save(newJob);
        List<Users> usersList=getUsersWithMatchingSkills(newJob.getJobId());
      //  sendEmailNotifications(usersList,newJob);
        return newJob;


    }
    // Delete

    public void deleteJob(Long id){
        jobRepo.deleteById(id);
    }
    public List<Job> displayFilteredPaginatedJobs(String query, String location, String jobType, String postedOn,
                                                  String remoteHybridOnsite,Integer yoe,Integer salary,List<Long> skillIds) {
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
        Predicate yoeQuery = (yoe != null) ? builder.equal(root.get("minYear"), yoe) : builder.conjunction();
// Create predicate for salary filter
        Predicate salaryQuery = (salary != null) ? builder.and(
                builder.greaterThanOrEqualTo(root.get("minSalary"), salary),
                builder.lessThanOrEqualTo(root.get("maxSalary"), salary)
        ) : builder.conjunction();
        // Create predicate for skill IDs filter
        Join<Job, Skills> skillJoin = root.join("skills", JoinType.INNER);
        Predicate skillQuery = (!skillIds.isEmpty()) ? skillJoin.get("id").in(skillIds) : builder.conjunction();



        //building query
        criteriaQuery.where(builder.or(queryInTitle,queryInDesc),locationQuery,jobTypeQuery,postedOnQuery,workTypeQuery,activeJobs,yoeQuery,salaryQuery,skillQuery);
        //typedQuery for future purposes
        TypedQuery<Job> jobTypedQuery=entityManager.createQuery(criteriaQuery);
        return jobTypedQuery.getResultList();
    }

    public Collection<?> listInterestedApplicants(Long jobId) {
        Job job=jobRepo.findById(jobId).get();

        List<JobSkills> jobSkillsList = job.getJobSkillsList();
        Collection<UserSkills> usersSet=new HashSet<>();

        jobSkillsList.forEach(jobSkills ->{
            usersSet.addAll(userSkillsRepo.findBySkills(jobSkills.getSkill()));
        } );
        Collection<String> usersCollection=new HashSet<>();
        usersSet.forEach(userSkills ->{
            if(userSkills.getUserProfile().getUsers().isEnableNotification())usersCollection.add(userSkills.getUserProfile().getUsers().getEmail());
        } );
        return usersCollection;
    }
    public void setStatus(Long jobId, boolean active, Principal principal) {
        Users user=usersRepo.findByEmail(principal.getName());
        Job job = jobRepo.findById(jobId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"No such job exists"));
        if(employerService.findByUsers(user).getCompany()
                .equals(job.getEmployer().getCompany())){
            job.setActive(active);
            jobRepo.save(job);
        }
    }

    public List<Users> getUsersWithMatchingSkills(Long jobId) {
        Job job=jobRepo.findById(jobId).get();
        List<JobSkills> requiredSkills=job.getJobSkillsList();
        Set<UserSkills> userSet=new HashSet<>();
        requiredSkills.forEach(jobSkillss ->{
                  userSet.addAll(userSkillsRepo.findBySkills(jobSkillss.getSkill()));
        });
        List<Users> usersSet=new ArrayList<>();
        userSet.forEach(userSkills -> usersSet.add(userSkills.getUserProfile().getUsers()));
        System.out.println(usersSet);
        return usersSet;
    }
    @Async
    private void sendEmailNotifications(List<Users> users, Job job) {
        for (Users users1 : users) {
                String body = "Dear " + usersService.getUserProfile(users1).getFullName() + ",\n"
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
                usersService.sendEmail(to, subject, body);
            }
        }
    }
    public List<Application> getListOfApplicants(Long jobId){
        Job job=jobRepo.findById(jobId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Job doesn't exist"));
        return job.getApplicationList();
    }
    public void deletePostedJob(Long jobId){
        jobRepo.deleteById(jobId);
    }
    public List<Categories> showJobCategories(){
        return categoriesRepo.findAll();
    }
}
