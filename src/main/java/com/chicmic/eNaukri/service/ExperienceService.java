package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.UserExperienceDto;
import com.chicmic.eNaukri.model.Experience;
import com.chicmic.eNaukri.model.UserExperience;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.ExperienceRepo;
import com.chicmic.eNaukri.repo.UserExperienceRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ExperienceService {
    private final UsersRepo usersRepo;
    private final CompanyRepo companyRepo;
    private final ExperienceRepo experienceRepo;
    private final UserExperienceRepo userExperienceRepo;
    private final UsersService usersService;
    public void addExperience(Principal principal, UserExperienceDto dto){
        Users user=usersRepo.findByEmail(principal.getName());
        boolean hasDuplicateJoinedDate = usersService.getUserProfile(user).getExperienceList().stream()
                .anyMatch(e -> (e.getJoinedOn().isBefore(dto.getJoinedOn()) && e.getEndedOn().isAfter(dto.getJoinedOn()))
                    || (e.getJoinedOn().isBefore(dto.getEndedOn()) && e.getEndedOn().isAfter(dto.getEndedOn()))
                );

        if (hasDuplicateJoinedDate) {
            throw new ApiException(HttpStatus.CONFLICT,"A duplicate joinedOn date exists for the user's experience.");
        }

        // Check if the user already has a current company
        boolean hasCurrentCompany = usersService.getUserProfile(user).getExperienceList().stream()
                .anyMatch(Experience::isCurrentlyWorking);

        if (dto.isCurrentlyWorking() && hasCurrentCompany) {
            throw new ApiException(HttpStatus.CONFLICT,"User can have only one currently working company.");
        }
        ObjectMapper mapper = CustomObjectMapper.createObjectMapper();
        Experience experience = mapper.convertValue(dto, Experience.class);
        experience.setExCompany(companyRepo.getById(dto.getCompanyId()));
        experience.setExpUser(usersService.getUserProfile(user));
        UserExperience userExperience=new UserExperience();
        userExperience.setExperience(experience);
        userExperience.setUser(user);
        experienceRepo.save(experience);
        userExperienceRepo.save(userExperience);
    }
    public void deleteExperience(Long expId){
        experienceRepo.deleteById(expId);
    }
}
