package com.example.android.fueltracker;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.fueltracker.data.UserContract;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements FavoritesRecyclerAdapter.ListItemClickListener
{
    private View rootView;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ArrayList<Favorites> favoriteStations = new ArrayList<>();
    private FavoritesRecyclerAdapter favoritesRecyclerAdapter;


    public FavoritesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_favorites, container, false);
        readFromDatabase();

        recyclerView = rootView.findViewById(R.id.favoritesListRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(this, favoriteStations);
        recyclerView.setAdapter(favoritesRecyclerAdapter);

        emptyView = rootView.findViewById(R.id.emptyFavortiesList);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(favoriteStations.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            favoritesRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void addFavorite()
    {
        favoriteStations.clear();
        readFromDatabase();
        if(favoriteStations.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        favoritesRecyclerAdapter.notifyDataSetChanged();
    }

    private void readFromDatabase()
    {
        String[] projection = {UserContract.FavoriteEntry.COLUMN_STATION, UserContract.FavoriteEntry.COLUMN_ADDRESS, UserContract.FavoriteEntry.COLUMN_CITY, UserContract.FavoriteEntry.COLUMN_STATION_ID};
        Cursor c = getActivity().getContentResolver().query(UserContract.FavoriteEntry.CONTENT_URI, projection, null, null, null);

        try
        {
            int stationIndex = c.getColumnIndex(UserContract.FavoriteEntry.COLUMN_STATION);
            int addressIndex = c.getColumnIndex(UserContract.FavoriteEntry.COLUMN_ADDRESS);
            int cityIndex = c.getColumnIndex(UserContract.FavoriteEntry.COLUMN_CITY);
            int idIndex = c.getColumnIndex(UserContract.FavoriteEntry.COLUMN_STATION_ID);
            if(c.moveToFirst())
                do
                {
                    String station = c.getString(stationIndex);
                    String address = c.getString(addressIndex);
                    String city = c.getString(cityIndex);
                    String id = c.getString(idIndex);
                    favoriteStations.add(new Favorites(id, station, address, city));
                }while (c.moveToNext());
        }

        finally
        {
            c.close();
        }
    }

    @Override
    public void AddressClick(int position)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + favoriteStations.get(position).getAddress() + ", " + favoriteStations.get(position).getCity());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
