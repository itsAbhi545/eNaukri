package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.ApiResponse;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.service.EmployerService;
import com.chicmic.eNaukri.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerSearchController {
    private  final JobService jobService;
    private final EmployerService employerService;
    @GetMapping("/search/users/{jobId}")
    public ApiResponse searchUsers(@PathVariable Long jobId,@ModelAttribute(name = "skill") Long[] skill) {
        Job job = jobService.getJobById(jobId);
        Set<Users> usersSet =  employerService.searchUsersForJob(job,skill);
        return new ApiResponse("Relevant User List Created Successfully", usersSet, HttpStatus.CREATED);
    }

    @GetMapping("jobs")
    public List<Job> displayJobs(@RequestParam(required = false,name = "q") String query,
                                 @RequestParam(required = false,name = "location") String location,
                                 @RequestParam(required = false,name = "type") String jobType,
                                 @RequestParam(required = false,name = "postedOn") String postedOn,
                                 @RequestParam(required = false,name = "remoteHybridOnsite") String remoteHybridOnsite){
        return jobService.displayFilteredPaginatedJobs(query,location,jobType,postedOn,remoteHybridOnsite);
    }

}
