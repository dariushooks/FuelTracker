package com.example.android.fueltracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.StationViewHolder>
{
    private static int NOT_FAVORITE = 0;
    private static int IS_FAVORITE = 1;
    private ArrayList<GasStation> stations;
    private StationRecyclerAdapter.ListItemClickListener listener;

    public interface ListItemClickListener
    {
        void AddressClick(int position);
        void FavoriteClick(int position);
    }

    public StationRecyclerAdapter(StationRecyclerAdapter.ListItemClickListener listener, ArrayList<GasStation> stations)
    {
        this.listener = listener;
        this.stations = stations;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public StationRecyclerAdapter.StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == NOT_FAVORITE)
            view = inflater.inflate(R.layout.station_list_not_favorite, parent, false);
        else
            view = inflater.inflate(R.layout.station_list_is_favorite, parent, false);
        return new StationRecyclerAdapter.StationViewHolder(view);
    }

    @Override
    public long getItemId(int position) { return Integer.parseInt(stations.get(position).getId()); }

    @Override
    public int getItemViewType(int position)
    {
        if(stations.get(position).getFavorite())
            return IS_FAVORITE;
        return NOT_FAVORITE;
    }

    @Override
    public void onBindViewHolder(@NonNull StationRecyclerAdapter.StationViewHolder holder, int position) { holder.bind(position); }


    @Override
    public int getItemCount() { return stations.size(); }

    class StationViewHolder extends RecyclerView.ViewHolder
    {
        private TextView stationName;
        private TextView stationAddress;
        private TextView stationCity;
        private TextView stationDistance;
        private TextView regPrice;
        private TextView midPrice;
        private TextView premPrice;
        private ImageView stationLogo;
        private ImageFilterView favoriteButton;
        private View favoriteButtonOverlay;
        private MotionLayout motionLayout;

        public StationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            motionLayout = itemView.findViewById(R.id.stationContainer);
            stationName = itemView.findViewById(R.id.stationName);
            stationAddress = itemView.findViewById(R.id.stationAddress);
            stationCity = itemView.findViewById(R.id.stationCity);
            stationDistance = itemView.findViewById(R.id.stationDistance);
            stationLogo = itemView.findViewById(R.id.stationLogo);
            regPrice = itemView.findViewById(R.id.regularPrice);
            midPrice = itemView.findViewById(R.id.midPrice);
            premPrice = itemView.findViewById(R.id.premiumPrice);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            favoriteButtonOverlay = itemView.findViewById(R.id.favoriteButtonOverlay);

            stationAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int clickedPosition = getAdapterPosition();
                    listener.AddressClick(clickedPosition);
                }
            });

            favoriteButtonOverlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int clickedPosition = getAdapterPosition();
                    listener.FavoriteClick(clickedPosition);
                }
            });

            motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
                @Override
                public void onTransitionStarted(MotionLayout motionLayout, int i, int i1)
                {

                }

                @Override
                public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v)
                {

                }

                @Override
                public void onTransitionCompleted(MotionLayout motionLayout, int i)
                {
                    int clickedPosition = getAdapterPosition();
                    listener.FavoriteClick(clickedPosition);
                }

                @Override
                public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v)
                {

                }
            });
        }

        public void bind(int position)
        {
            stationName.setText(stations.get(position).getName());
            stationAddress.setText(stations.get(position).getAddress());
            stationCity.setText(stations.get(position).getCity());
            stationDistance.setText(stations.get(position).getDistance());
            regPrice.setText(stations.get(position).getReg());
            midPrice.setText(stations.get(position).getMid());
            premPrice.setText(stations.get(position).getPrem());

            switch (stations.get(position).getName())
            {
                case "Arco": stationLogo.setImageResource(R.drawable.arco); break;
                case "Mobil": stationLogo.setImageResource(R.drawable.mobil); break;
                case "Shell": stationLogo.setImageResource(R.drawable.shell); break;
                case "Chevron": stationLogo.setImageResource(R.drawable.chevron); break;
                case "76": stationLogo.setImageResource(R.drawable.seventysix); break;
                case "Valero": stationLogo.setImageResource(R.drawable.valero); break;
                case "Texaco": stationLogo.setImageResource(R.drawable.texaco); break;
                case "Exxon": stationLogo.setImageResource(R.drawable.exxon); break;
                case "7-Eleven": stationLogo.setImageResource(R.drawable.seveneleven); break;
                case "Sinclair": stationLogo.setImageResource(R.drawable.sinclair); break;
                default: stationLogo.setImageResource(R.drawable.generic); break;
            }
        }
    }
}
