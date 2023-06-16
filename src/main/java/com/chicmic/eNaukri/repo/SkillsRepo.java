package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillsRepo extends JpaRepository< Skills,Long> {
    @Query(
            "Select s from Skills s where s.skillName like concat('%',:query,'%')"
    )
    List<Skills> findSkillsBySkillName(@Param("query") String query);

    boolean existsBySkillName(String skillName);
}
