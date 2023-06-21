//package com.chicmic.eNaukri.controller;
//
//import com.chicmic.eNaukri.Dto.ApiResponse;
//import com.chicmic.eNaukri.model.Job;
//import com.chicmic.eNaukri.model.Skills;
//import com.chicmic.eNaukri.service.JobService;
//import com.chicmic.eNaukri.service.SkillsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class SkillSearchController {
//    private final SkillsService skillsService;
//
//    @GetMapping("search-skills")
//    public List<Skills> displaySkills(@RequestParam String query){
//        return skillsService.findBySkillName(query);
//    }
//}
