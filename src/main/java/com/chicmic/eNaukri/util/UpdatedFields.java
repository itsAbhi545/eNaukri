package com.chicmic.eNaukri.util;

import lombok.extern.log4j.Log4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j
public class UpdatedFields {


    public static List<String> findUpdatedFields(Object objA, Object objB) throws IllegalAccessException {
        List<String> changedProperties = new ArrayList<>();
        for (Field field : objA.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value1 = field.get(objA);
            Object value2 = field.get(objB);
            if (value1 != null && value2 != null) {
                log.info(field.getName() + "1=" + value1);
                log.info(field.getName() + "2=" + value2);
                if (!Objects.equals(value1, value2)) {
                    changedProperties.add(field.getName());
                }
            }
        }
       return changedProperties;
    }

}
