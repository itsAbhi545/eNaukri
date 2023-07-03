package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.TrimNullValidator.Trim;
import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TrimAll
public class SocialLinkDto {

    private String linkedIn;
    private String twitter;
    private String others;
    private String facebook;
}
