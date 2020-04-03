package com.example.complaintapp;

public class Response {
    public String Corporation_User_Id;
    public String Citizen_User_Id;
    public String Complaint_Id;
    public String Response_Text;
    public String Image_Url;
    public String Date;

    public Response() {
    }

    public Response(String corporation_User_Id, String citizen_User_Id, String complaint_Id, String response_Text, String image_Url, String date) {
        Corporation_User_Id = corporation_User_Id;
        Citizen_User_Id = citizen_User_Id;
        Complaint_Id = complaint_Id;
        Response_Text = response_Text;
        Image_Url = image_Url;
        Date = date;
    }
}
