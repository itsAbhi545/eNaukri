package com.chicmic.eNaukri.Dto;

import com.chicmic.eNaukri.TrimNullValidator.TrimAll;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TrimAll
public class CategoriesDto {
    private String name;
    private List<Long> categorySkills = new ArrayList<Long>();

}
