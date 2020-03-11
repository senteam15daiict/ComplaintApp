package com.example.complaintapp;

public class Citizen {
    public String name;
    public String password;
    public String email;
    public String phoneNumber;

    public Citizen(){

    }

    public Citizen(String name, String password, String email, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
