package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.Dto.UserEducationDto;
import com.chicmic.eNaukri.model.Education;
import com.chicmic.eNaukri.model.UserEducation;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.EducationRepo;
import com.chicmic.eNaukri.repo.UserEducationRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service public class EducationService {
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    EducationRepo educationRepo;
    @Autowired
    UserEducationRepo userEducationRepo;
    public void addEducation(UserEducationDto dto,Long userId){
        Users user=usersRepo.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"User doesn't exist"));
        boolean hasDuplicateDegree = user.getEducationList().stream()
                .anyMatch(e -> e.getDegree().equalsIgnoreCase(dto.getDegree()));
        if (hasDuplicateDegree) {
            throw new ApiException(HttpStatus.CONFLICT,"A duplicate degree entry already exists for the user.");
        }
        if(dto.getStartFrom().isAfter(LocalDate.now())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Start date cannot be in the future.");
        }

        ObjectMapper mapper = CustomObjectMapper.createObjectMapper();
        Education education = mapper.convertValue(dto, Education.class);
        if(education.getEndOn().isBefore(LocalDate.now())){
            education.setStudent(false);
        }
        else{
            education.setStudent(true);
        }
        education.setEdUser(user);
        UserEducation userEducation=new UserEducation();
        userEducation.setEducation(education);
        userEducation.setUser(user);
        educationRepo.save(education);
        userEducationRepo.save(userEducation);
    }
//    public void updateEducation(Long userId,UserEducationDto dto){
//        Users user=usersRepo.findById(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"User doesn't exist"));
//        if(dto.getUniversityName()!=null);
//    }

}
