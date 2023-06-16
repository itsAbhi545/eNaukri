package com.chicmic.eNaukri.config;//package com.chicmic.eNaukri.config;
//
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
        Users admin=userService.getUserByEmail("admin@gmail.com");
        if(admin==null){
           // admin=Users.builder().email("admin@gmail.com").phoneNumber("9987654321").fullName("Harman").password(passwordEncoder().encode("Harman@1234")).build();
//            userService.saveUser(admin);
        }
    }
}
