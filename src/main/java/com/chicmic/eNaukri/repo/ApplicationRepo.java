package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepo extends JpaRepository<Application,Long> {
    boolean existsByApplicantIdAndJobId(Users temp, Job job);

    Application findByApplicantIdAndJobId(Users temp, Job job);
}
