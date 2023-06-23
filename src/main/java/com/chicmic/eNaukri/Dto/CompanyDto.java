package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.CompanyCategories;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private String email;
    private String password;
    private String phoneNumber;
    private Company company;
    private List<Long> categories;

}
