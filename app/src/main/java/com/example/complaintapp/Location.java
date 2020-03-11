package com.example.complaintapp;

public class Location {
    public String Country;
    public String State;
    public String District;

    public Location(){

    }

    public Location(String country,String state,String district){
        this.Country = country.toUpperCase();
        this.State = state.toUpperCase();
        this.District = district.toUpperCase();
    }

}
