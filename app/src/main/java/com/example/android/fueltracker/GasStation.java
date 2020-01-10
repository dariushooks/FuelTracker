package com.example.android.fueltracker;

public class GasStation
{
    private String address;
    private String city;
    private String name;
    private String distance;
    private String reg;
    private String mid;
    private String prem;
    private String id;
    private boolean favorite;

    public GasStation(String station_id, String station_name, String station_address, String station_city, String station_distance, String reg_price, String mid_price, String prem_price)
    {
        id = station_id;
        name = station_name;
        address = station_address;
        distance = station_distance;
        reg = reg_price;
        mid = mid_price;
        prem = prem_price;
        city = station_city;
    }

    public String getId(){return id;}
    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getDistance(){return distance;}
    public String getReg(){return reg;}
    public String getMid(){return mid;}
    public String getPrem(){return prem;}
    public String getCity() {return city;}
    public boolean getFavorite(){return favorite;}
    public void setFavorite(boolean favorite){this.favorite = favorite;}
}
