package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.EmployerDto;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private  final UsersService usersService;
    private final CompanyService companyService;




    public Users saveEmployer(Users users, MultipartFile userImg,MultipartFile companyImg) throws IOException {
        Employer employer = users.getEmployerProfile();
        Company company = users.getEmployerProfile().getEmployerCompany();

        employer.setPpPath( FileUploadUtil.imageUpload(userImg));
        company.setPpPath(FileUploadUtil.imageUpload(companyImg));

        Set<Employer> employerSet = companyService.findEmployerByEmail(company.getEmail());
        employerSet.add(employer);
        company.setEmployerSet(employerSet);
        employer.setEmployerCompany(company);
        employer.setUsers(users);
        users.setEmployerProfile(employer);
        return usersService.register(users);

    }
}
