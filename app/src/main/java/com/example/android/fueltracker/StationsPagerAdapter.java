package com.example.android.fueltracker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class StationsPagerAdapter extends FragmentStatePagerAdapter
{
    StationFragment.FavoritesCommunicator favoritesCommunicator;
    public StationsPagerAdapter(FragmentManager fm, StationFragment.FavoritesCommunicator favoritesCommunicator)
    {
        super(fm);
        this.favoritesCommunicator = favoritesCommunicator;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0: return new StationFragment(favoritesCommunicator);
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
