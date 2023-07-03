package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepo extends JpaRepository<Education,Long> {
    Education findByDegree(String degree);
}
