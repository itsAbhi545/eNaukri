package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.UserSkillDto;
import com.chicmic.eNaukri.model.Skills;
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
    public void addSkills(UserSkillDto dto){
        Long userId = dto.getUserId();
        List<Long> skillIds = dto.getSkillIds();
        Users user = usersRepo.findByUserId(userId);
        List<Skills> selectedSkills = skillsRepo.findAllById(skillIds);
        for (Skills skill : selectedSkills) {
            UserSkills userSkills=new UserSkills();
            userSkills.setUser(user);
            userSkills.setSkills(skill);
            user.getUserSkillsList().add(userSkills);
            userSkillsRepo.save(userSkills);
        }
        usersRepo.save(user);
    }
}
