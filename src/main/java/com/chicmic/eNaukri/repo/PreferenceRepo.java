package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.Preference;
import com.chicmic.eNaukri.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepo extends JpaRepository<Preference, Long> {

//    @Query(
//            value = "SELECT userPreferences FROM preference p WHERE STRCMP(p.location ,:#{#job.location}) = 0 " +
//                    "AND STRCMP(p.remoteHybridOnsite,:#{#job.remoteHybridOnsite}) = 0 " +
//                    "AND :#{#job.minSalary} <= p.salary AND p.salary <= :#{#job.maxSalary} " +
//                    "AND :#{#job.minYear} <= p.yop AND p.yop <= :#{#job.maxYear}",
//            nativeQuery = true
//    )
    @Query(
            value = "SELECT userPreferences FROM preference p WHERE STRCMP(p.location , 'hello') = 0 " +
                    "AND STRCMP(p.remoteHybridOnsite,'hello') = 0 " +
                    "AND 100000 <= p.salary AND p.salary <= 100000 " +
                    "AND 100000 <= p.yop AND p.yop <= 100000",
            nativeQuery = true
    )
    List<UserProfile> searchUserPreferencesByJob(Job Job);
}
