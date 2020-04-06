package com.example.android.fueltracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class StationsViewFragment extends Fragment implements StationFragment.FavoritesCommunicator
{
    private ViewPager viewPager;
    private StationsPagerAdapter adapter;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_stationview, container, false);

        viewPager = rootView.findViewById(R.id.viewpager);

        adapter = new StationsPagerAdapter(getActivity().getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void favoriteClicked()
    {
        //String tag = "android:switcher:" + R.id.viewpager + ":" + 1;
        FavoritesFragment favoritesFragment = (FavoritesFragment) getFragmentManager().getFragments().get(2);
        favoritesFragment.addFavorite();
    }
}
