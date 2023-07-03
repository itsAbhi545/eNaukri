package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.Dto.SocialLinkDto;
import com.chicmic.eNaukri.model.SocialLink;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.CompanyRepo;
import com.chicmic.eNaukri.repo.SocialLinkRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.util.CustomObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLinkService {
    private final SocialLinkRepo socialLinkRepo;
    private final UsersRepo usersRepo;
    private final CompanyRepo companyRepo;
     public void addSocialLinks(Long userId, SocialLinkDto dto){

            Users user= usersRepo.findByUserId(userId);
            SocialLink socialLink = CustomObjectMapper.convertDtoToObject(dto,SocialLink.class);
            socialLink.setUser(user);
            user.setSocialLink(socialLink);
            socialLinkRepo.save(socialLink);

    }
    public void deleteSocialLinks(Long userId){
         socialLinkRepo.deleteById(userId);
    }
}
