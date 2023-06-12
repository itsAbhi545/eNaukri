package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.validation.RegEx;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDto {
    @NotNull
    private String fullName;
    @Email
    @Pattern(regexp = RegEx.EMAIL)
    @NotNull
    private String email;
    @Pattern(regexp = RegEx.PASSWORD,message = "Please enter a valid password")
    @NotNull
    private String password;

    @Pattern(regexp = RegEx.PHONENUMBER,message = "Please enter a valid number")
    @NotNull
    private String phoneNumber;
    private String zipCode;
    private String address;
    private String gstId;
    private String iso;
    private String website;
    private String company;
    private String position;

}
