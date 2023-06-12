package com.chicmic.eNaukri.validation;

public class RegEx {
    public final static String EMAIL = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    public final static String PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    public final static  String PHONENUMBER = "\\+?\\d[\\d -]{8,12}\\d";
    public boolean matchEmail(String email){
        return email.matches(EMAIL)
                ;
    }
    public boolean matchPassword(String password){
        return password.matches(PASSWORD);
    }
}