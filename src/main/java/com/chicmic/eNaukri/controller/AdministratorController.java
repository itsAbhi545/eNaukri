package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.model.ApplicationStatus;
import com.chicmic.eNaukri.model.Categories;
import com.chicmic.eNaukri.model.Roles;
import com.chicmic.eNaukri.model.Skills;
import com.chicmic.eNaukri.service.AdministratorService;
import com.chicmic.eNaukri.service.CompanyService;
import com.chicmic.eNaukri.service.RolesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
public class AdministratorController {
    private final AdministratorService administratorService;
    private final RolesService rolesService;
    private final CompanyService companyService;
    @PostMapping("create-application-status")
    public ApiResponse createApplicationStatus(@RequestBody ApplicationStatus applicationStatus){
         applicationStatus=administratorService.createApplicationStatus(applicationStatus);
        return new ApiResponse("Created new status",applicationStatus,HttpStatus.CREATED);
    }
    @PostMapping("approve-company")
    public ApiResponse approve(Long id){
        administratorService.approveCompany(id);
        return new ApiResponse("Company approved",companyService.findCompanyById(id),HttpStatus.CREATED);
    }
    @PostMapping("create-job-categories")
    public ApiResponse createCategories(@RequestBody @Valid Categories categories){
        categories =administratorService.createCategories(categories);
        return new ApiResponse("created new category", categories,HttpStatus.CREATED);
    }
    @PostMapping("create-skills")
    public ApiResponse addNewSkills(Skills skills){
        skills=administratorService.createSkills(skills);
        return new ApiResponse("skill created",skills,HttpStatus.CREATED);
    }
    @PostMapping("/add-role")
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
    @DeleteMapping("/soft-delete/{userId}")
    public void softDelete(@PathVariable Long userId){
        administratorService.softDelete(userId);
    }
}
