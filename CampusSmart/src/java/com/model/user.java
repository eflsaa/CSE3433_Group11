/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

/**
 *
 * @author User
 */
public class user {
    private String matricNumber;
    private String password;
    private String name;
    private String email;
    private String course;

    // Constructor for login validation
    public user(String matricNumber, String password) {
        this.matricNumber = matricNumber;
        this.password = password;
    }

    // Full constructor for user creation
    public user(String matricNumber, String password, String name, String email, String course) {
        this.matricNumber = matricNumber;
        this.password = password;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    // Getters and Setters
    public String getMatricNumber() { 
        return matricNumber; 
    }
    public void setMatricNumber(String matricNumber) { 
        this.matricNumber = matricNumber; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getCourse() { 
        return course; 
    }
    public void setCourse(String course) { 
        this.course = course; 
    }
}
