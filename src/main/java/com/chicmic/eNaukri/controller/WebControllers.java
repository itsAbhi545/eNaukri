package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hadoop.shaded.com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class WebControllers {
    @GetMapping("/signup")
    public String signup(){return "signup";}
    @GetMapping("/checkout")
    public String checkout(){
        return "checkout";
    }
    @GetMapping("/success/{CHECKOUT_SESSION_ID}")
    public String success(@PathVariable String CHECKOUT_SESSION_ID ){
        return "success";
    }

//    @GetMapping("/customer-portal")
//    public String success(HttpServletRequest request, HttpServletResponse response){
//        String url ="http://localhost:8081/create-portal-session?session_id="+ request.getSession().getId();
//        RestTemplate restTemplate = new RestTemplate();
////        restTemplate.postForEntity(url,request, String.class);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> request1 =
//                new HttpEntity<String>(null, headers);
//
//        String personResultAsJsonStr =
//                restTemplate.postForObject(url, request1, String.class);
//        return "success";
//    }
}
