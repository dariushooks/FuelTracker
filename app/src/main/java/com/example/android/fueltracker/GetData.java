package com.example.android.fueltracker;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetData extends AsyncTaskLoader
{
    private String gasUrl;
    private String data = "";
    private String id;
    private String reg;
    private String mid;
    private String prem;
    private String station_name;
    private String address;
    private String city;
    private String distance;

    public GetData(@NonNull Context context, String request_url)
    {
        super(context);
        gasUrl = request_url;
    }

    @Override
    protected void onStartLoading()
    {
        super.onStartLoading();
        StationFragment.progressText.setText("RUNNING...");
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground()
    {
        try
        {
            Thread.sleep(0);
            URL url = new URL(gasUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream input = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String line = bufferedReader.readLine();
            while(line != null)
            {
                data = data + line;
                line = bufferedReader.readLine();
            }

            input.close();
            bufferedReader.close();
            connection.disconnect();

            data = data.replace("?(","");
            data = data.replace(")","");

            JSONObject JO1 = new JSONObject(data);
            JSONArray JA = JO1.getJSONArray("stations");
            for(int i = 0; i < JA.length(); i++)
            {
                JSONObject JO = (JSONObject) JA.get(i);
                id = JO.getString("id");
                reg = JO.getString("reg_price");
                mid = JO.getString("mid_price");
                prem = JO.getString("pre_price");
                station_name = JO.getString("station");
                address = JO.getString("address");
                city = JO.getString("city") + ", " + JO.getString("region");
                distance = JO.getString("distance");
                if(distance.equals("1 miles"))
                    distance = distance.replace("miles","mile");
                StationFragment.stations.add(new GasStation(id, station_name, address, city, distance, reg, mid, prem));
            }
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
        catch (JSONException e) { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return null;
    }
}
