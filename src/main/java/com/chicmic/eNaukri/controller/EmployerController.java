//package com.chicmic.eNaukri.controller;
//
//import ch.qos.logback.core.joran.util.beans.BeanUtil;
//import com.chicmic.eNaukri.Dto.ApiResponse;
//import com.chicmic.eNaukri.Dto.EmployerDto;
//import com.chicmic.eNaukri.Dto.UsersDto;
//import com.chicmic.eNaukri.model.*;
//import com.chicmic.eNaukri.service.EmployerService;
//import com.chicmic.eNaukri.service.UsersService;
//import com.chicmic.eNaukri.util.CustomObjectMapper;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("/employer")
//@RequiredArgsConstructor
//public class EmployerController {
//    private final EmployerService employerService;
//    private final RolesService rolesService;
//
//    @PostMapping("/signup")
//    public ApiResponse signup(@Valid @RequestBody EmployerDto employerDto) {
//        Users users = employerService.saveEmployer(employerDto);
//        Roles roles = rolesService.getRoleByRoleName("EMPLOYER");
//        UserRole userRole = UserRole.builder()
//                .userId(users)
//                .roleId(roles)
//                .build();
//        rolesService.saveUserRole(userRole);
//        return new ApiResponse( "User Register Successfully as Employer", users.getEmployerProfile(), HttpStatus.CREATED );
//    }
//    @PostMapping("/invite{jobId}/{userId}")
//    public ApiResponse invite(@PathVariable Long jobId, @PathVariable Long userId, Principal principal){
//        Users user=employerService.inviteForJob(userId,jobId,principal);
//        return new ApiResponse("user invited",user.getUserProfile().getInvitedJobs(),HttpStatus.OK);
//    }
//}
