package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.SocialLink;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLinkRepo extends JpaRepository< SocialLink,Long> {
    SocialLink findByUser(Users user);
}
