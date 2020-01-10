package com.example.android.fueltracker;

public class Favorites
{
    private String id;
    private String name;
    private String address;
    private String city;

    public Favorites(String station_id, String station_name, String station_address, String station_city)
    {
        id = station_id;
        name = station_name;
        address = station_address;
        city = station_city;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getId() {return id;}

}
