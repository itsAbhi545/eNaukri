package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    UserRole findByUserId(Users userId);
}
