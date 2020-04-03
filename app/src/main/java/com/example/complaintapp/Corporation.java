package com.example.complaintapp;

import java.util.HashMap;
import java.util.Map;

public class Corporation {
    public String User_name;
    public String Email;
    public String Phone_Number;
    public String Password;
    public String Security_Key;
    public Location location;
    public String Profile_Image_Url;
    public Map<String,String> Types;

    public Corporation(){

    }

    public Corporation(String user_name,String email,String phone_Number,String password,String security_key,Location loc){
        this.User_name = user_name;
        this.Email = email;
        this.Phone_Number = phone_Number;
        this.Password = password;
        this.Security_Key = security_key;
        this.location = loc;
        this.Profile_Image_Url = "";
        this.Types = new HashMap<String, String>();
        this.Types.put("dead_animal","0");
        this.Types.put("garbage_dump","0");
        this.Types.put("stagnant_water","0");
    }

    public Corporation(String user_name, String email, String phone_Number, String password, String security_Key, Location location, String profile_Image_Url, Map<String, String> types) {
        User_name = user_name;
        Email = email;
        Phone_Number = phone_Number;
        Password = password;
        Security_Key = security_Key;
        this.location = location;
        Profile_Image_Url = profile_Image_Url;
        Types = types;
    }
}
