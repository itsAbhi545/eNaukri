package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.repo.PreferenceRepo;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;
    private final PreferenceRepo preferenceRepo;
    private final EmployerRepo employerRepo;


    public Users saveEmployer(Users users , MultipartFile userImg,MultipartFile companyImg) throws IOException {
        Employer employer = users.getEmployerProfile();
        employer.setIsApproved(false);
        Company company = users.getEmployerProfile().getCompany();

        employer.setPpPath( FileUploadUtil.imageUpload(userImg));
        company.setPpPath(FileUploadUtil.imageUpload(companyImg));
        Company companyIfExist = companyService.getCompanyByEmail(company.getEmail());
        if(companyIfExist != null){
            company = companyIfExist;
        }
        Set<Employer> employerSet = companyService.findEmployerByEmail(company.getEmail());
        employerSet.add(employer);
        company.setEmployerSet(employerSet);
        employer.setCompany(company);
        employer.setUsers(users);
        users.setEmployerProfile(employer);
        return usersService.register(users);

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



}
