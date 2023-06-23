package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepo extends JpaRepository<Company,Long> {
    Company findByName(String name);
    @Query(
            "select c from Company c where c.users.email = :email"
    )
    Company findByEmail(@Param("email") String email);
    Company findByUuid(String uuid);
    Company findByUsers(Users users);

    @Query(
            "SELECT company FROM Company company WHERE (company.name LIKE  %:query% Or company.users.email LIKE  %:query% Or " +
                    " company.users.phoneNumber LIKE %:query% Or company.address LIKE %:location% Or " +
                    "company.zipCode = :location) "
    )
    List<Company> findCompanyByQuery(@Param("query") String query, @Param("location") String location);
}
