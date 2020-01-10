package com.example.android.fueltracker;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StationsPagerAdapter extends FragmentPagerAdapter
{
    public StationsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0: return new StationFragment();
            case 1: return new FavoritesFragment();
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0: return "Stations";
            case 1: return "Favorites";
            default: return null;
        }
    }


    @Override
    public int getCount()
    {
        return 2;
    }
}
