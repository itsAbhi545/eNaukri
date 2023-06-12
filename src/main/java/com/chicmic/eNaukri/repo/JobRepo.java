package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JobRepo extends JpaRepository<Job,Long> {

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndPostedOnAfterAndActive(String location, String jobType, String remoteHybridOnsite, LocalDate startDate, boolean b);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndJobTitleContainingIgnoreCaseAndActive(String location, String jobType, String remoteHybridOnsite, String query, boolean b);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndActive(String location, String jobType, String remoteHybridOnsite, boolean b);

    List<Job> findByLocationContainingIgnoreCaseAndJobTypeAndRemoteHybridOnsiteAndPostedOnAfterAndJobTitleContainingIgnoreCaseAndActive(String location, String jobType, String remoteHybridOnsite, LocalDate startDate, String query, boolean b);
    Job findJobByJobId(Long jobId);
}
