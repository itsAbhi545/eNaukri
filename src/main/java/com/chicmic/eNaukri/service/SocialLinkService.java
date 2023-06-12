package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.SocialLinkDto;
import com.chicmic.eNaukri.model.Application;
import com.chicmic.eNaukri.model.Company;
import com.chicmic.eNaukri.model.SocialLink;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.SocialLinkRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLinkService {
    private final SocialLinkRepo socialLinkRepo;
    private final UsersRepo usersRepo;
    private final CompanyRepo companyRepo;
     public void addSocialLinks(Long userId, SocialLinkDto dto, Long companyId){
        if(userId!=null&&companyId==null){
            Users user= usersRepo.findByUserId(userId);
            SocialLink socialLink = CustomObjectMapper.convertDtoToObject(dto,SocialLink.class);
            socialLink.setUserLinks(user);
            socialLinkRepo.save(socialLink);
        }
        if (userId==null&&companyId!=null){
            Company company=companyRepo.findById(companyId).get();
            SocialLink socialLink = CustomObjectMapper.convertDtoToObject(dto, SocialLink.class);
            socialLink.setCompanyLinks(company);
            socialLinkRepo.save(socialLink);
        }
    }
}
