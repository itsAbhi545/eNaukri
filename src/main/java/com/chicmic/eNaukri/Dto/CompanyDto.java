package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.Company;
import lombok.*;

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
}
