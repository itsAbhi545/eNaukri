package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepo extends JpaRepository<Employer, Long> {

}
