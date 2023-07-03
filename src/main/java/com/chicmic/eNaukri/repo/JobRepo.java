package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Job;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JobRepo extends JpaRepository<Job,Long> {

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndPostedOnAfterAndActive(String location, String jobType, String remoteHybridOnsite, LocalDate startDate, boolean b);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndJobTitleContainingIgnoreCaseAndActive(String location, String jobType, String remoteHybridOnsite, String query, boolean b);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndActive(String location, String jobType, String remoteHybridOnsite, boolean b);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndPostedOnAfterAndJobTitleContainingIgnoreCaseAndActive(String location, String jobType, String remoteHybridOnsite, LocalDate startDate, String query, boolean b);
    Job findJobByJobId(Long jobId);
    @Transactional
    @Modifying
    @Query(
            value = "delete from Job j where j.jobId =?1"
    )
    void deleteJobByJobId(Long jobId);

}
