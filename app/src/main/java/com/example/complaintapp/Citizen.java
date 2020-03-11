package com.example.complaintapp;

public class Citizen {
    public String User_Name;
    public String Password;
    public String Email;
    public String PhoneNumber;

    public Citizen(){

    }

    public Citizen(String name, String password, String email, String phoneNumber) {
        this.User_Name = name;
        this.Password = password;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }
}
