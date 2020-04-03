package com.example.complaintapp;

public class Citizen {
    public String User_Name;
    public String Password;
    public String Email;
    public String Phone_Number;
    public String Profile_Image_Url;

    public Citizen(){

    }

    public Citizen(String name, String password, String email, String phoneNumber) {
        this.User_Name = name;
        this.Password = password;
        this.Email = email;
        this.Phone_Number = phoneNumber;
        this.Profile_Image_Url = "";
    }
}
