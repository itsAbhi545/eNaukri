package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.Employer;
import com.chicmic.eNaukri.model.UserProfile;
import com.chicmic.eNaukri.validation.RegEx;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.web.multipart.MultipartFile;

//import javax.validation.constraints.Pattern;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor public class UsersDto {


    @Pattern(regexp = RegEx.EMAIL,message = "not a valid email")
    private String email;
    @Pattern(regexp = RegEx.PHONENUMBER,message = "phone needs to be 10 digits")
    private String phoneNumber;
    private String password;
    private Employer employerProfile;
    private UserProfile userProfile;
    private Long companyId;
//    private String currentCompany;
//    private String bio;

}
