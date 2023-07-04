package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Users userId);
}
