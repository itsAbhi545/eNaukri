package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepo extends JpaRepository<Company,Long> {
    Company findByName(String name);
    Company findByEmail(String email);
}
