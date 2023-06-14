package com.chicmic.eNaukri.config;

import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;

@Configuration
public class AdminConfig {
    @Autowired
    UserServiceImpl userService;
    @Bean
    public void createAdmin(){
        Users admin=userService.getUserByEmail("hermano@gmail.com");
        if(admin==null){
            admin=Users.builder().email("hermano@gmail.com")
                    .phoneNumber("9987654321")
                    .fullName("Harman")
                    .password(passwordEncoder().encode("Harman@1234"))
                    .isVerified(true)
                    .build();
            userService.saveUser(admin);
        }
    }
}
