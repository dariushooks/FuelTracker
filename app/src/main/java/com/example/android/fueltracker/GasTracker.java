package com.example.android.fueltracker;



public class GasTracker
{
    private String date;
    private String spent;
    private String gallons;
    private String price;
    private String station;
    private String id;

    public GasTracker(String user_id, String user_date, String user_spent, String user_gallons, String user_price, String user_station)
    {
        id = user_id;
        date = user_date;
        spent = user_spent;
        gallons = user_gallons;
        price = user_price;
        station = user_station;
    }


    public String getId()  {return id;}
    public String getDate() {return date;}
    public String getSpent() {return spent;}
    public String getGallons() {return gallons;}
    public String getPrice() {return price;}
    public String getStation() {return station;}
}
