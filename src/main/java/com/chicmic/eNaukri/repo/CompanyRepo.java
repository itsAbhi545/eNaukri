package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<Company,Long> {
    Company findByCompanyName(String name);
}
