package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Service@RequiredArgsConstructor
public class AdministratorService {
    private final EmployerRepo employerRepo;
    private final UsersRepo usersRepo;
    private final ApplicationStatusRepo applicationStatusRepo;
    private final JobCategoriesRepo jobCategoriesRepo;
    private final SkillsRepo skillsRepo;
    private final RolesService rolesService;
    private final CompanyRepo companyRepo;
    public ApplicationStatus createApplicationStatus(ApplicationStatus dto){
        ApplicationStatus applicationStatus=applicationStatusRepo.save(dto);
        return applicationStatus;
    }
    public void approveCompany(Long companyId){
        Company company=companyRepo.findById(companyId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"company doesn't exist"));
        if(company.isApproved()){
            company.setApproved(false);
        }
        else{
            company.setApproved(true);
        }
    }
    public JobCategories createJobCategories(JobCategories dto){
        JobCategories jobCategories=jobCategoriesRepo.save(dto);
        return jobCategories;
    }
    public Skills createSkills(Skills dto){
        if(skillsRepo.existsBySkillName(dto.getSkillName())){
            throw new ApiException(HttpStatus.CONFLICT,"A skill by the same skill name exists");
        }
        Skills newSkill=skillsRepo.save(dto);
        return newSkill;
    }
    public void softDelete(Long userId){
        Users user= usersRepo.findById(userId).get();
        UserRole userRole=rolesService.findUserRoleByUser(user);
        if(userRole.isDeleted()){
            userRole.setDeleted(false);
        }
        else{
            userRole.setDeleted(true);
        }
    }
//    public Users editProfile(Principal principal,){
//        Users admin = usersRepo.findByEmail(principal.getName());
//        admin.setPassword(passwordEncoder().encode("Harman@1234"));
//    }
    public List<Users> getUsers(){
        return  usersRepo.findAll();
    }
}
