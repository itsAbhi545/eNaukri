package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.model.Roles;
import com.chicmic.eNaukri.model.UserRole;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.EmployerService;
import com.chicmic.eNaukri.service.RolesService;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;
    private final RolesService rolesService;

    @PostMapping("/signup")
    public ApiResponse signup(@Valid Users users, @ModelAttribute MultipartFile userImg,
                              @ModelAttribute MultipartFile companyImg) throws IOException {
        System.out.println("sdjhsgdfgsd");
//        users = employerService.saveEmployer(users, userImg, companyImg);
//        Roles roles = rolesService.getRoleByRoleName("EMPLOYER");
//        UserRole userRole = UserRole.builder()
//                .userId(users)
//                .roleId(roles)
//                .build();
//        rolesService.saveUserRole(userRole);
        return new ApiResponse( "User Register Successfully as Employer", "users.getEmployerProfile()", HttpStatus.CREATED );
    }
    @PostMapping("/addRole")
    public String addRole(@RequestParam String roleName){
        Roles roles = rolesService.getRoleByRoleName(roleName);
        if(roles == null){
            roles = Roles.builder()
                  .roleName(roleName.toUpperCase())
                  .build();
            rolesService.saveRoles(roles);
        }
        return "Successfully Added " + roleName;
    }


}
