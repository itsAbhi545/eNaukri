package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.model.UserSkills;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserSkillsRepo extends JpaRepository<UserSkills,Long> {
    Collection<UserSkills> findBySkills(Skills jobSkill);
}
