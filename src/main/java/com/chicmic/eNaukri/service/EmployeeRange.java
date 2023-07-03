package com.chicmic.eNaukri.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeRange {
    Map<String,String>map=new HashMap<>();
    public void createRange(){
        map.put("1","1-50");
        map.put("2","51-200");
        map.put("3","201-500");
        map.put("4","501-1000");
        map.put("5","1001-5000");
        map.put("6","5001-10000");
        map.put("7","10000+");
    }
    public String getRange(String key){
        createRange();
        return map.get(key);
    }
}
