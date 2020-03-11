package com.example.complaintapp;

public class Corporation {
    public String User_name;
    public String Email;
    public String Phone_Number;
    public String Password;
    public String Security_Key;
    public Location location;

    public Corporation(){

    }

    public Corporation(String user_name,String email,String phone_Number,String password,String security_key,Location loc){
        this.User_name = user_name;
        this.Email = email;
        this.Phone_Number = phone_Number;
        this.Password = password;
        this.Security_Key = security_key;
        this.location = loc;
    }
}
