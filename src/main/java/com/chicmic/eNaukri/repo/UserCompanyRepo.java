package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCompanyRepo extends JpaRepository<UserCompany,Long> {
}
