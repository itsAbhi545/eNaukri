package com.chicmic.eNaukri.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.*;
import com.chicmic.eNaukri.repo.EmployerRepo;
import com.chicmic.eNaukri.repo.PreferenceRepo;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    public Employer findByUsers(Users users){
        return employerRepo.findByUsers(users);
    }


}
