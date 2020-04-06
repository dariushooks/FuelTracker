package com.example.android.fueltracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.fueltracker.data.UserContract;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.android.fueltracker.App.isConnected;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements LoaderManager.LoaderCallbacks, StationRecyclerAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener
{
    private final String TAG = StationFragment.class.getSimpleName();
    public static ArrayList<GasStation> stations = new ArrayList<>();
    private ArrayList<Favorites> favorites = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private String url;
    private ProgressBar progress;
    private TextView progressText;
    private TextView emptyConnection;
    private TextView emptyData;
    private RecyclerView recyclerView;
    private StationRecyclerAdapter stationRecyclerAdapter;
    private View rootView;
    private FavoritesCommunicator favoritesCommunicator;
    private SwipeRefreshLayout refreshLayout;

    public StationFragment(FavoritesCommunicator favoritesCommunicator)
    {
        this.favoritesCommunicator = favoritesCommunicator;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        createLocationRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        rootView = getLayoutInflater().inflate(R.layout.fragment_station, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        refreshLayout = rootView.findViewById(R.id.stationRefresh);
        refreshLayout.setOnRefreshListener(this);

        progress = rootView.findViewById(R.id.progressBar);
        progressText = rootView.findViewById(R.id.progressText);
        emptyData = rootView.findViewById(R.id.emptyElementData);
        emptyConnection = rootView.findViewById(R.id.emptyElementConnection);

        recyclerView = rootView.findViewById(R.id.stationListRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        stationRecyclerAdapter = new StationRecyclerAdapter(this, stations);
        recyclerView.setAdapter(stationRecyclerAdapter);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(isConnected)
        {
            readFromDatabase();
            LoaderManager.getInstance(this).initLoader(0,null, this);
        }

        else
        {
            progress.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh()
    {
        recyclerView.setVisibility(View.GONE);
        if(isConnected)
        {
            emptyConnection.setVisibility(View.GONE);
            readFromDatabase();
            LoaderManager.getInstance(this).restartLoader(0, null, this);
        }

        else
        {
            progress.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyConnection.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        }
    }

    private void createLocationRequest()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getLocation()
    {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location)
            {
                if(location != null)
                {
                    Log.i(TAG, "LOCATION LAST");
                    currentLocation = location;
                    Log.i(TAG, "LATITUDE: " + currentLocation.getLatitude());
                    Log.i(TAG, "LONGITUDE: " + currentLocation.getLongitude());
                    url = "http://api.mygasfeed.com/stations/radius/"
                            + currentLocation.getLatitude()
                            + "/" + currentLocation.getLongitude()
                            + "/5/reg/distance/e5ieinrc85.json?callback=?";
                    GetData getData = (GetData) LoaderManager.getInstance(StationFragment.this).getLoader(0);
                    getData.setGasUrl(url);
                    getData.forceLoad();
                }

                else
                {
                    Log.i(TAG, "LOCATION UPDATE");
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                }
            }
        });
    }

    private LocationCallback locationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            super.onLocationResult(locationResult);
            for(Location location : locationResult.getLocations())
            {
                if(location != null)
                {
                    currentLocation = location;
                    Log.i(TAG, "LATITUDE: " + currentLocation.getLatitude());
                    Log.i(TAG, "LONGITUDE: " + currentLocation.getLongitude());
                    url = "http://api.mygasfeed.com/stations/radius/"
                            + currentLocation.getLatitude()
                            + "/" + currentLocation.getLongitude()
                            + "/5/reg/distance/e5ieinrc85.json?callback=?";
                    GetData getData = (GetData) LoaderManager.getInstance(StationFragment.this).getLoader(0);
                    if(getData != null)
                    {
                        getData.setGasUrl(url);
                        getData.forceLoad();
                    }
                }
            }

            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    };

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args)
    {
        getLocation();
        return new GetData(Objects.requireNonNull(getActivity()));
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data)
    {
        progress.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        if (stations.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
            emptyConnection.setVisibility(View.GONE);
            emptyData.setVisibility(View.VISIBLE);
        }

        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyData.setVisibility(View.GONE);
            emptyConnection.setVisibility(View.GONE);
            if(!favorites.isEmpty())
                for (int i = 0; i < stations.size(); i++)
                    for (int j = 0; j < favorites.size(); j++)
                        if(stations.get(i).getAddress().equals(favorites.get(j).getAddress()))
                            stations.get(i).setFavorite(true);

            stationRecyclerAdapter.notifyDataSetChanged();
        }

        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {}

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
                    favorites.add(new Favorites(id, station, address, city));
                }while (c.moveToNext());
        }

        finally
        {
            c.close();
        }
    }

    private void deleteFromDatabase(int i)
    {
        String where = UserContract.FavoriteEntry.COLUMN_ADDRESS;
        String[] w = {stations.get(i).getAddress()};

        Uri uri = Uri.withAppendedPath(UserContract.FavoriteEntry.CONTENT_URI,stations.get(i).getAddress());

        getActivity().getContentResolver().delete(uri, where, w);
    }

    private void writeToDatabase(int i)
    {
        ContentValues values = new ContentValues();
        values.put(UserContract.FavoriteEntry.COLUMN_STATION, stations.get(i).getName());
        values.put(UserContract.FavoriteEntry.COLUMN_ADDRESS, stations.get(i).getAddress());
        values.put(UserContract.FavoriteEntry.COLUMN_CITY, stations.get(i).getCity());
        values.put(UserContract.FavoriteEntry.COLUMN_STATION_ID, stations.get(i).getId());

        getActivity().getContentResolver().insert(UserContract.FavoriteEntry.CONTENT_URI, values);
    }

    @Override
    public void AddressClick(int position)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + stations.get(position).getAddress() + ", " + stations.get(position).getCity());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void FavoriteClick(int position)
    {
        if(stations.get(position).getFavorite())
        {
            stations.get(position).setFavorite(false);
            deleteFromDatabase(position);
            favoritesCommunicator.favoriteClicked();
        }

        else
        {
            stations.get(position).setFavorite(true);
            writeToDatabase(position);
            favoritesCommunicator.favoriteClicked();
        }

        //stationRecyclerAdapter.notifyItemChanged(position);
    }

    public interface FavoritesCommunicator
    {
        void favoriteClicked();
    }
}


