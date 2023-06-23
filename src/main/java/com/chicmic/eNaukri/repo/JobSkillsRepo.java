package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.JobSkills;
import com.chicmic.eNaukri.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillsRepo extends JpaRepository<JobSkills,Long> {
    List<JobSkills> findByJob(Job job);
    List<JobSkills> findBySkill(Skills skills);
}
