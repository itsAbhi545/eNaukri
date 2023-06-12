package com.chicmic.eNaukri.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.EmployerService;
import com.chicmic.eNaukri.service.UsersService;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody EmployerDto employerDto) {
        employerService.saveEmployer(employerDto);
        return "signup";

    }

}
