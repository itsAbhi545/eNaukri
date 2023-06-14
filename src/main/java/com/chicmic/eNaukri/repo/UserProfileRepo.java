package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfile,Long> {
}
