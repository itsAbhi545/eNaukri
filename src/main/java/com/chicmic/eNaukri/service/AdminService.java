package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.repo.SkillsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final SkillsRepo skillsRepo;


    public Skills createSkills(Skills dto) {
        dto.setSkillName(dto.getSkillName().toLowerCase());
        return skillsRepo.save(dto);

    }

}
