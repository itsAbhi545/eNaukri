package com.chicmic.eNaukri.util;

import java.lang.reflect.Field;

public class CompletionStatus {
    public static Double calCompletionStatus(Object obj) throws IllegalAccessException {
        Long nullFields = 0L;
        Long total = 0L;
        for (Field field : obj.getClass().getDeclaredFields()){
            if((field.getType() == String.class || field.getType() == Long.class ) && !field.getName().equals("id")) {
                field.setAccessible(true);
                System.out.println("\u001B[35m" + field.getName() + ": " + field.get(obj) + "\u001B[0m");
                if (field.get(obj) == null || field.get(obj).equals("")) {
                    nullFields++;
                }
                total++;
            }
        }
        System.out.println("\u001B[35m" + nullFields + " : " + total + "\u001B[0m");


        return (double) (((total - nullFields) * 100)/total);
    }
}
