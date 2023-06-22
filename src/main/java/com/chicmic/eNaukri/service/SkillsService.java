package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UserSkillDto;
import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.model.UserProfile;
import com.chicmic.eNaukri.model.UserSkills;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.SkillsRepo;
import com.chicmic.eNaukri.repo.UserSkillsRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillsService {
    private final UsersRepo usersRepo;
    private final SkillsRepo skillsRepo;
    private final UserSkillsRepo userSkillsRepo;
    private final UsersService usersService;
    public void addSkills(UserSkillDto dto){
        Long userId = dto.getUserId();
        List<Long> skillIds = dto.getSkillIds();
        Users user = usersRepo.findByUserId(userId);
        UserProfile userProfile = usersService.getUserProfile(user);
        List<Skills> selectedSkills = skillsRepo.findAllById(skillIds);
        for (Skills skill : selectedSkills) {
            UserSkills userSkills=new UserSkills();
            userSkills.setUserProfile(userProfile);
            userSkills.setSkills(skill);
            usersService.getUserProfile(user).getUserSkillsList().add(userSkills);
            userSkillsRepo.save(userSkills);
        }
        usersRepo.save(user);
    }
    public List<Skills> findBySkillName(String name){
        return skillsRepo.findSkillsBySkillName(name);
    }
}
