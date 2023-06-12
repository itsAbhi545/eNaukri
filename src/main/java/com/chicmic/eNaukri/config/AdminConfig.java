//package com.chicmic.eNaukri.config;
//
//import com.chicmic.eNaukri.model.Users;
//import com.chicmic.eNaukri.service.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AdminConfig {
//    @Autowired
//    UserServiceImpl userService;
//    @Bean
//    public void createAdmin(){
//        Users admin=userService.getUserByEmail("harman@gmail.com");
//        if(admin==null){
//            admin=Users.builder().email("hermano@gmail.com").phoneNumber("0987654321").fullName("Harman").password("Harman@chigdfgdcmic").ppPath("assets/img/admin.jpg").build();
//            userService.saveUser(admin);
//        }
//    }
//}
