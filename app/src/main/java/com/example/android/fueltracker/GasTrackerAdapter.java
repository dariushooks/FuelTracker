package com.example.android.fueltracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GasTrackerAdapter extends ArrayAdapter<GasTracker>
{
    public GasTrackerAdapter(Activity context, ArrayList<GasTracker> stations)
    {
        super(context,0,stations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.history_list, parent, false);

        GasTracker currentTracker = getItem(position);

        TextView rowTextView = listItemView.findViewById(R.id.rowID);
        rowTextView.setText(currentTracker.getId());

        TextView dateTextView = listItemView.findViewById(R.id.purchaseDate);
        dateTextView.setText(currentTracker.getDate());

        TextView spentTextView = listItemView.findViewById(R.id.amountSpent);
        spentTextView.setText(currentTracker.getSpent());

        TextView gallonsTextView = listItemView.findViewById(R.id.gallons);
        gallonsTextView.setText(currentTracker.getGallons());

        TextView priceTextView = listItemView.findViewById(R.id.price);
        priceTextView.setText(currentTracker.getPrice());

        TextView stationTextView = listItemView.findViewById(R.id.gasStation);
        stationTextView.setText(currentTracker.getStation());

        return listItemView;
    }
}
