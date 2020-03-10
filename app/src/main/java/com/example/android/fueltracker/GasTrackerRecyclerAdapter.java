package com.example.android.fueltracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GasTrackerRecyclerAdapter extends RecyclerView.Adapter<GasTrackerRecyclerAdapter.GasTrackerViewHolder>
{
    private Context context;
    private ArrayList<GasTracker> tracker;
    private GasTrackerRecyclerAdapter.ListItemClickListener listener;
    private ConstraintLayout container;
    private TextView date;
    private TextView spent;
    private TextView gallons;
    private TextView price;
    private TextView station;

    public interface ListItemClickListener
    {
        void onClick(int position, View view);
    }

    public GasTrackerRecyclerAdapter(GasTrackerRecyclerAdapter.ListItemClickListener listener, ArrayList<GasTracker> tracker)
    {
        this.tracker = tracker;
        this.listener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public GasTrackerRecyclerAdapter.GasTrackerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profile_list, parent, false);
        GasTrackerRecyclerAdapter.GasTrackerViewHolder viewHolder = new GasTrackerRecyclerAdapter.GasTrackerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GasTrackerViewHolder holder, int position) { holder.bind(position); }

    @Override
    public int getItemCount() { return tracker.size(); }

    class GasTrackerViewHolder extends RecyclerView.ViewHolder
    {

        public GasTrackerViewHolder(@NonNull View itemView)
        {
            super(itemView);
            container = itemView.findViewById(R.id.listContainer);
            date = itemView.findViewById(R.id.purchaseDate);
            spent = itemView.findViewById(R.id.amountSpent);
            gallons = itemView.findViewById(R.id.gallons);
            price = itemView.findViewById(R.id.price);
            station = itemView.findViewById(R.id.gasStation);
        }

        public void bind(final int position)
        {
            date.setText(tracker.get(position).getDate());
            spent.setText(tracker.get(position).getSpent());
            gallons.setText(tracker.get(position).getGallons());
            price.setText(tracker.get(position).getPrice());
            station.setText(tracker.get(position).getStation());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    listener.onClick(position, container);
                }
            });
        }
    }
}
