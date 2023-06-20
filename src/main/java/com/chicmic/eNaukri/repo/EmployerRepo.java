package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerRepo extends JpaRepository<Employer, Long> {
    @Query(
            "SELECT e FROM Employer e WHERE (e.fullName LIKE  %:query% Or e.users.email LIKE  concat('%' , :query, '%') Or " +
                    " e.users.phoneNumber LIKE %:query% Or e.company.name LIKE %:query% " +
                    "  )"
    )
    public List<Employer> findEmployersByAnyMatch(@Param("query")String query);
    public Employer findByUsers(Users users);
}
