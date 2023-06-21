package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.CompanyCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCategoriesRepo extends JpaRepository<CompanyCategories, Long> {

}
