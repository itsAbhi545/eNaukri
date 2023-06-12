package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UserTokenRepo extends JpaRepository<UserToken,Long> {
    UserToken findByToken(String token);
    @Transactional
    @Modifying
    void deleteUserTokenByToken(String value);
}
