package com.chicmic.eNaukri.config;

import com.chicmic.eNaukri.model.Roles;
import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.RolesService;
import com.chicmic.eNaukri.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminConfig {
    private final UserServiceImpl userService;
    private final RolesService rolesService;
    @Bean
    public void createAdmin(){
        Users admin=userService.getUserByEmail("hermano@gmail.com");
        if(admin==null){
            String uuid=UUID.randomUUID().toString();
            admin=Users.builder().email("hermano@gmail.com")
                    .phoneNumber("9987654321")
                    .password(passwordEncoder().encode("Harman@1234"))
                    .uuid(uuid)
                    .isVerified(true)
                    .build();
            Roles roles = rolesService.getRoleByRoleName("EMPLOYER");
            UserRole userRole = UserRole.builder()
                    .userId(admin)
                    .roleId(roles)
                    .build();
            rolesService.saveUserRole(userRole);
            userService.saveUser(admin);
        }
    }
}
