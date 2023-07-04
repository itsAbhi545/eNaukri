package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;
    private final ApplicationRepo applicationRepo;
    private final JobRepo jobRepo;
    private final ExperienceRepo experienceRepo;
    private final SkillsRepo skillsRepo;
    private final CompanyRepo companyRepo;
    private final UserTokenRepo tokenRepo;
    private final RolesService rolesService;
    private final EmployerService employerService;
    private final CompanyService companyService;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    JavaMailSender javaMailSender;



    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader=request.getHeader("Authorization").substring(7);
        System.out.println(authHeader);
        tokenRepo.deleteUserTokenByToken(authHeader);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user= usersRepo.findByEmail(username);
        if(user==null){
            throw new ApiException(HttpStatus.NOT_FOUND,"User does not exist");
        }
        if(user.isVerified()==false ){
            throw new ApiException(HttpStatus.BAD_REQUEST,"User is not verified");
        }
//        if(!user.getEmployerProfile().getIsApproved()) {
//            throw new ApiException(HttpStatus.UNAUTHORIZED,"Employer is not approved");
//        }
        if(employerService.findByUsers(user)!=null&&!employerService.findByUsers(user).getIsApproved()) {
            throw new ApiException(HttpStatus.FORBIDDEN,"Employer is not approved");
        }
        if(companyService.findCompanyByUser(user)!=null&&!companyService.findCompanyByUser(user).isApproved()){
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,"Company needs to be approved by the admin before login");
        }
        List<UserRole> userRoleList = rolesService.findUserRoleByUser(user);
        for(UserRole userRole : userRoleList) {
            if (userRole.isDeleted()) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "User is Deleted");
            }
        }
        Collection<Authority> authorities=new ArrayList<>();
        for(UserRole userRole : userRoleList) {
            authorities.add(new Authority("ROLE_" + userRole.getRoleId().getRoleName()));
        }
        return new User(user.getEmail(),user.getPassword(),authorities);
    }
    public Users loginResponse(Principal principal){
        Users  user= usersRepo.findByEmail(principal.getName());
        return user;
    }

    public String findCurrentCompany(Long id) {
        Users users=usersRepo.findById(id).get();
       return experienceRepo.findByExpUserAndCurrentlyWorking(users,true).getExCompany().getName();
    }

    public void changeAlerts(Principal principal, boolean b) {
        Users temp=usersRepo.findByEmail(principal.getName());
        temp.setEnableNotification(b);
        usersRepo.save(temp);
    }

    public boolean checkJobForUser(Principal principal, Long jobId) {
        Users temp=usersRepo.findByEmail(principal.getName());
        Job job=jobRepo.findById(jobId).get();
        return applicationRepo.existsByApplicantIdAndJobId(temp,job);
    }

    public void withdrawApxn(Principal principal, Long jobId) {
        Users temp=usersRepo.findByEmail(principal.getName());
        Job job=jobRepo.findById(jobId).get();
        Application application= applicationRepo.findByApplicantIdAndJobId(temp,job);
        application.setWithdraw(true);
        applicationRepo.save(application);
        job.setNumApplicants(job.getNumApplicants()-1);
        applicationRepo.delete(application);
    }
    public Users getUserByUuid(String uuid) { return usersRepo.findByUuid(uuid); }

}
