package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;

    public Users saveEmployer(EmployerDto employerDto) {
        Users user = new Users();
        BeanUtils.copyProperties(employerDto, user);

        Employer employer = new Employer();
        BeanUtils.copyProperties(employerDto, employer);

        Company company = new Company();
        BeanUtils.copyProperties(employerDto, company);

        List<Employer> employerList = companyService.findEmployerByEmail(company.getEmail());
        employerList.add(employer);
        company.setEmployerList(employerList);
        employer.setEmployerCompany(company);
        employer.setUsers(user);
        user.setEmployerProfile(employer);
        return usersService.register(user);

    }
}
