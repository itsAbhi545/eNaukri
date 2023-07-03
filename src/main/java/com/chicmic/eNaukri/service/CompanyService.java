package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.CompanyDto;
import com.chicmic.eNaukri.Dto.SocialLinkDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {
    private final UsersRepo usersRepo;
    private final SocialLinkRepo socialLinkRepo;
    private final CompanyCategoriesRepo companyCategoriesRepo;
    private final EmployeeRange employeeRange;
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
        companyDto.getCompany().setCompletionStatus(80.0);
        Company company = save(companyDto.getCompany());
        List<Categories> categoriesList=categoriesRepo.findAllById(companyDto.getCategories());
        List<CompanyCategories> companyCategoriesList = new ArrayList<>();
        for(Categories category : categoriesList){
            companyCategoriesList.add(new CompanyCategories(null, company, category));
        }
        companyCategoriesRepo.saveAll(companyCategoriesList);
        //Role
        rolesService.addRoleToUser("COMPANY", users);
        return company;
    }
    public Company updateCompany(Company companyReq,SocialLinkDto dto, Principal principal,MultipartFile companyImg,String key, String date) throws IOException {
        Users user = usersService.getUserByEmail(principal.getName());
        Company company = findCompanyByUser(user);
        if(company==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"The user is not registered as company");
        }
        if(LocalDate.parse(date).isAfter(LocalDate.now())){
            throw new ApiException(HttpStatus.FORBIDDEN,"The founding date can not be in the future");
        }
        if(!companyImg.isEmpty()){
            company.setPpPath(FileUploadUtil.imageUpload(companyImg));
        }
        SocialLink socialLink=socialLinkRepo.findByUser(user);
        if(socialLink!=null){
        if(!dto.getLinkedIn().isBlank()&&dto.getLinkedIn()!=null)socialLink.setLinkedIn(dto.getLinkedIn());
        if(!dto.getFacebook().isBlank()&&dto.getFacebook()!=null)socialLink.setFacebook(dto.getFacebook());
        if(!dto.getTwitter().isBlank()&&dto.getTwitter()!=null)socialLink.setTwitter(dto.getTwitter());
        if(!dto.getOthers().isBlank()&&dto.getOthers()!=null)socialLink.setTwitter(dto.getOthers());
        socialLinkRepo.save(socialLink);
        }
        else{
            SocialLink socialLink1=CustomObjectMapper.convertDtoToObject(dto, SocialLink.class);
            socialLinkRepo.save(socialLink1);
        }
        company.getCompletionStatus();
        if(companyReq.getName()!=null&&!companyReq.getName().isBlank())
            company.setName(companyReq.getName());
        if(companyReq.getAddress()!=null&&!companyReq.getAddress().isBlank())
            company.setAddress(companyReq.getAddress());
        if(date!=null&&!date.isEmpty()&&!date.isBlank())
            company.setFoundedIn(LocalDate.parse(date));
        if(companyReq.getWebsite()!=null&&!companyReq.getWebsite().isBlank())
            company.setWebsite(companyReq.getWebsite());
        if(companyReq.getAbout()!=null&&!companyReq.getAbout().isBlank())
            company.setAbout(companyReq.getAbout());
        if(key!=null&&!key.isBlank())
            company.setSize(employeeRange.getRange(key));
        companyRepo.save(company);
        return company;
    }
    public Company findByID(Long id) {
        return companyRepo.findById(id).orElse(null);
    }
    public Company findByUUID(String uuid) {
        return companyRepo.findByUuid(uuid);
    }
    public Company findCompanyByUser(Users users) {
        return companyRepo.findByUsers(users);
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
    public void deletedCompany(Long id) {
        companyRepo.deleteById(id);
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

    public List<Company> searchCompany(String query, String location, List<Long> categoryIds){
        return companyCategoriesRepo.searchCompanyAndCategory(companyRepo.findCompanyByQuery(query, location), categoryIds);
    }
    public Company createCompanyProfile(Principal principal, String key, MultipartFile companyImg, SocialLinkDto dto, String date) throws IOException {
        Users user = usersService.getUserByEmail(principal.getName());
        Company company= findCompanyByUser(user);
        if(company==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"The user does not exist or isn't signed up as company");
        }
        if(LocalDate.parse(date).isAfter(LocalDate.now())){
            throw new ApiException(HttpStatus.FORBIDDEN,"The date can't be in the future");
        }
        double count1=0;
        if(key!=null){count1++ ;}
        if(date!=null){count1++;}
        if(companyImg!=null){count1++;}
        double count2=0;
        if(dto.getLinkedIn()!=null){
            count2++;
        }
        if(dto.getTwitter()!=null){
            count2++;
        }
        if(dto.getFacebook()!=null){
            count2++;
        }
        System.out.println(count2+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(count1+"???????????????????????????????");
        double completePercentage=((count1+(count2/3))/4)*20;
        System.out.println(completePercentage);
        company.setCompletionStatus(80+completePercentage);
        company.setFoundedIn(LocalDate.parse(date));
        company.setSize(employeeRange.getRange(key.trim()));
        company.setPpPath(FileUploadUtil.imageUpload(companyImg));
        SocialLink sl= socialLinkRepo.findByUser(user);
        if(sl==null){
        SocialLink socialLink = CustomObjectMapper.convertDtoToObject(dto, SocialLink.class);
        socialLink.setUser(user);
        user.setSocialLink(socialLink);
        socialLinkRepo.save(socialLink);}
        else{
            sl.setTwitter(dto.getTwitter());
            sl.setLinkedIn(dto.getLinkedIn());
            sl.setFacebook(dto.getFacebook());
            sl.setOthers(dto.getOthers());
            socialLinkRepo.save(sl);
        }
        usersRepo.save(user);
        companyRepo.save(company);
        return company;
    }
}
