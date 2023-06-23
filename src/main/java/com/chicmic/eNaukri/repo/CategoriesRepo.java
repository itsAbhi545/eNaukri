package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepo extends JpaRepository<Categories,Long> {

}
