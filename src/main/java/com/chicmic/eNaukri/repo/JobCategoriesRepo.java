package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.JobCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoriesRepo extends JpaRepository<JobCategories,Long> {

}
