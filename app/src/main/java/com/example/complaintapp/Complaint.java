package com.example.complaintapp;

import org.xml.sax.Locator;

import java.util.Date;

public class Complaint {
    public String Citizen_User_Id;
    public String Corporation_User_Id;
    public Location location;
    public String date;
    public String Image_Url;
    public String Address;
    public String Type;
    public String Description;
    public String Status;
    public String Citizen_User_Name;
    public String Complaint_Id;
    public String lat;
    public String lon;

    public Complaint(){

    }

    public Complaint(String citizen_User_Id, String corporation_User_Id, Location loc, String d1, String image_Url, String address, String type, String description, String citizen_User_Name, String complaint_Id,String lat,String lon){
        this.Citizen_User_Id = citizen_User_Id;
        this.Corporation_User_Id = corporation_User_Id;
        this.location = loc;
        this.date = d1;
        this.Image_Url = image_Url;
        this.Address = address;
        this.Type = type;
        this.Description = description;
        this.Status = "Pending";
        this.Citizen_User_Name = citizen_User_Name;
        this.Complaint_Id = complaint_Id;
        this.lat = lat;
        this.lon = lon;
    }

}
