package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepo extends JpaRepository<Education,Long> {
}
