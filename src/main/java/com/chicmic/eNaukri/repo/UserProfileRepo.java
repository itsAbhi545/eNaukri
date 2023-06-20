package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserProfile;
import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    public UserProfile findUserProfileByUsers(Users users);
    @Query(
            "SELECT u FROM UserProfile u WHERE (u.fullName LIKE  %:query% Or u.users.email LIKE  %:query% Or " +
                    " u.users.phoneNumber LIKE %:query% )"
    )
    public List<UserProfile> findByQuery(@Param("query") String query);
}
