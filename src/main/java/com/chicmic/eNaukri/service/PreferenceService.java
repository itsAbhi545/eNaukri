package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.UserSkills;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.PreferenceRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final UsersRepo usersRepo;
    private final PreferenceRepo preferenceRepo;
//    public List<Job> getJobsByPreference(Principal principal){
//        Users user=usersRepo.findByEmail(principal.getName());
//        user.getUserProfile().getPreference();
//        for(UserSkills us:user.getUserProfile().getUserSkillsList()){
//            us.getSkills();
//        }
//
//    }
}
