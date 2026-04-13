package com.sp.web.web;

import lombok.Data;

@Data // Lombok automatically creates getters, setters, and a toString() method
public class UserCMD {
    
    private String username;
    private String password;
    private String email;
    private String name;
    private int age;
    
}