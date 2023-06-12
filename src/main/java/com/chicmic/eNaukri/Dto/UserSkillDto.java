package com.chicmic.eNaukri.Dto;

import lombok.Data;

import java.util.List;

@Data public class UserSkillDto {

        private Long userId;
        private List<Long> skillIds;

        // Constructors, getters, setters, etc.

}
