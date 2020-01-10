package com.example.android.fueltracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StationActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new StationFragment()).commit();
    }
}



