package com.example.android.fueltracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.fueltracker.data.UserContract;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment implements LoaderManager.LoaderCallbacks, StationRecyclerAdapter.ListItemClickListener
{

    public static ArrayList<GasStation> stations = new ArrayList<>();
    private ArrayList<Favorites> favorites = new ArrayList<>();
    private ProgressBar progress;
    public static TextView progressText;
    private TextView emptyConnection;
    private TextView emptyData;
    private RecyclerView recyclerView;
    private StationRecyclerAdapter stationRecyclerAdapter;
    private boolean connection;
    private View rootView;
    private FavoritesCommunicator favoritesCommunicator;

    //private View fave;
    //private RelativeLayout getDirections;
    //private int position;
    //private Animator circularReveal;
    //private Animator circularExit;



    public StationFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        rootView = getLayoutInflater().inflate(R.layout.activity_station, container, false);
        connection = getActivity().getIntent().getBooleanExtra("INTERNET_CONNECTION", false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
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
        if(connection)
        {
            progress.setVisibility(View.VISIBLE);
            emptyConnection.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            readFromDatabase();
            LoaderManager.getInstance(this).initLoader(0,null, this);
        }

        else
        {
            progress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyConnection.setVisibility(View.VISIBLE);
        }
    }

    /*private void openDialog(int i, View view)
    {
        position = i;
        final int revealX = (int) (view.getWidth()/2);
        final int revealY = (int) (view.getHeight()/2);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        final View alertLayout = getLayoutInflater().inflate(R.layout.station_alert_dialog,null);

        TextView reg = alertLayout.findViewById(R.id.regularPrice);
        reg.setText(stations.get(position).getReg());

        TextView mid = alertLayout.findViewById(R.id.midPrice);
        mid.setText(stations.get(position).getMid());

        TextView pre = alertLayout.findViewById(R.id.premiumPrice);
        pre.setText(stations.get(position).getPrem());

        TextView name = alertLayout.findViewById(R.id.stationName);
        name.setText(stations.get(position).getName());

        TextView address = alertLayout.findViewById(R.id.stationAddress);
        address.setText(stations.get(position).getAddress());

        TextView city = alertLayout.findViewById(R.id.stationCity);
        city.setText(stations.get(position).getCity());

        TextView distance = alertLayout.findViewById(R.id.stationDistance);
        distance.setText(stations.get(position).getDistance());

        ImageView logo = alertLayout.findViewById(R.id.stationLogo);
        switch (stations.get(position).getName())
        {
            case "Arco": logo.setImageResource(R.drawable.arco); break;
            case "Mobil": logo.setImageResource(R.drawable.mobil); break;
            case "Shell": logo.setImageResource(R.drawable.shell); break;
            case "Chevron": logo.setImageResource(R.drawable.chevron); break;
            case "76": logo.setImageResource(R.drawable.seventysix); break;
            case "Valero": logo.setImageResource(R.drawable.valero); break;
            case "Texaco": logo.setImageResource(R.drawable.texaco); break;
            case "Exxon": logo.setImageResource(R.drawable.exxon); break;
            case "7-Eleven": logo.setImageResource(R.drawable.seveneleven); break;
            case "Sinclair": logo.setImageResource(R.drawable.sinclair); break;
            default: logo.setImageResource(R.drawable.generic); break;
        }

        final ImageView exit = alertLayout.findViewById(R.id.exitDialog);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                float finalRadius = (float) (Math.max(alertLayout.getWidth(), alertLayout.getHeight()) * 1.1);

                // create the animator for this view (the start radius is zero)
                circularExit = ViewAnimationUtils.createCircularReveal(alertLayout, revealX, revealY, finalRadius, 0);
                circularExit.setDuration(1000);
                circularExit.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        alertLayout.setVisibility(View.INVISIBLE);
                        alertDialog.dismiss();
                    }
                });
                // make the view visible and start the animation
                circularExit.start();
            }
        });

        fave = alertLayout.findViewById(R.id.favoriteButton);
        if (stations.get(position).getFavorite())
            fave.setBackground(getActivity().getResources().getDrawable((R.drawable.ic_is_favorite)));
        else
            fave.setBackground(getActivity().getResources().getDrawable((R.drawable.ic_not_favorite)));
        fave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!stations.get(position).getFavorite())
                {
                    fave.setBackground(getActivity().getDrawable(R.drawable.ic_is_favorite));
                    stations.get(position).setFavorite(true);
                    stationAdapter.notifyDataSetChanged();
                    writeToDatabase(position);
                    favoritesCommunicator.favoriteClicked();
                }
                else
                {
                    fave.setBackground(getActivity().getDrawable(R.drawable.ic_not_favorite));
                    stations.get(position).setFavorite(false);
                    stationAdapter.notifyDataSetChanged();
                    deleteFromDatabase(position);
                    favoritesCommunicator.favoriteClicked();
                }
            }
        });

        getDirections = alertLayout.findViewById(R.id.stationNameAddress);
        getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + stations.get(position).getAddress() + ", " + stations.get(position).getCity());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                float finalRadius = (float) (Math.max(alertLayout.getWidth(), alertLayout.getHeight()) * 1.1);

                // create the animator for this view (the start radius is zero)
                circularReveal = ViewAnimationUtils.createCircularReveal(alertLayout, revealX, revealY, 0, finalRadius);
                circularReveal.setDuration(1000);
                circularReveal.setInterpolator(new AccelerateInterpolator());
                circularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        ViewCompat.animate(exit)
                                .rotation(135.0f)
                                .withLayer()
                                .setDuration(300)
                                .setInterpolator(new OvershootInterpolator(10.0f))
                                .start();
                    }
                });

                // make the view visible and start the animation
                alertLayout.setVisibility(View.VISIBLE);
                circularReveal.start();
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(alertLayout);

        new Dialog(getActivity().getApplicationContext());
        alertDialog.show();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        alertDialog.getWindow().setBackgroundDrawable(inset);
        alertDialog.getWindow().setElevation(20.0f);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,700);
    }*/


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args)
    {
        String request_url = getActivity().getIntent().getStringExtra("REQUEST_URL");
        return new GetData (getActivity(), request_url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data)
    {
        progress.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        if (stations.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
            emptyData.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyData.setVisibility(View.GONE);
            if(!favorites.isEmpty())
                for (int i = 0; i < stations.size(); i++)
                    for (int j = 0; j < favorites.size(); j++)
                        if(stations.get(i).getAddress().equals(favorites.get(j).getAddress()))
                            stations.get(i).setFavorite(true);

            stationRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) { }

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

        stationRecyclerAdapter.notifyItemChanged(position);
    }

    public interface FavoritesCommunicator
    {
        void favoriteClicked();
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            favoritesCommunicator = (FavoritesCommunicator) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString().trim() + " must implement favoriteClicked");
        }
    }
}


