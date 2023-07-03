package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriesRepo extends JpaRepository<Categories,Long> {
    @Query(
            "Select s from Categories s where s.name like concat('%',:query,'%')"
    )
    List<Categories> findCategoriesByName(@Param("query") String query);
}
