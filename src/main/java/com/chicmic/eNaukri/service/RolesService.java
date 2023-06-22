package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.model.Roles;
import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.RoleRepo;
import com.chicmic.eNaukri.repo.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RoleRepo roleRepo;
    private final UserRoleRepo userRoleRepo;
    public Roles saveRoles(Roles roles) {
        return roleRepo.save(roles);
    }
    public Roles getRoleByRoleName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    //USERROLE
    public UserRole saveUserRole(UserRole userRole) {
        return userRoleRepo.save(userRole);
    }
    public UserRole findUserRoleByUser(Users user) {
        return userRoleRepo.findByUserId(user);
    }
}
