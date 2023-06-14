package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationStatusRepo extends JpaRepository<ApplicationStatus,Long> {
}
