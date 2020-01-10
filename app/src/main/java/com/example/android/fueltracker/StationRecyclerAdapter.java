package com.example.android.fueltracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.StationViewHolder>
{

    private Context context;
    private ArrayList<GasStation> stations;
    private StationRecyclerAdapter.ListItemClickListener listener;
    private TextView stationName;
    private TextView stationAddress;
    private TextView stationCity;
    private TextView stationDistance;
    private TextView regPrice;
    private TextView midPrice;
    private TextView premPrice;
    private ImageView stationLogo;
    private View favoriteButton;
    private LinearLayout stationNameAddress;

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
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.station_list, parent, false);
        StationRecyclerAdapter.StationViewHolder viewHolder = new StationRecyclerAdapter.StationViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) { return Integer.parseInt(stations.get(position).getId()); }

    @Override
    public int getItemViewType(int position) { return Integer.parseInt(stations.get(position).getId()); }

    @Override
    public void onBindViewHolder(@NonNull StationRecyclerAdapter.StationViewHolder holder, int position) { holder.bind(position); }

    @Override
    public int getItemCount() { return stations.size(); }

    class StationViewHolder extends RecyclerView.ViewHolder
    {

        public StationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            stationName = itemView.findViewById(R.id.stationName);
            stationAddress = itemView.findViewById(R.id.stationAddress);
            stationCity = itemView.findViewById(R.id.stationCity);
            stationDistance = itemView.findViewById(R.id.stationDistance);
            stationLogo = itemView.findViewById(R.id.stationLogo);
            regPrice = itemView.findViewById(R.id.regularPrice);
            midPrice = itemView.findViewById(R.id.midPrice);
            premPrice = itemView.findViewById(R.id.premiumPrice);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            stationNameAddress = itemView.findViewById(R.id.stationNameAddress);

            stationNameAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int clickedPosition = getAdapterPosition();
                    listener.AddressClick(clickedPosition);
                }
            });

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int clickedPosition = getAdapterPosition();
                    listener.FavoriteClick(clickedPosition);
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

            if(stations.get(position).getFavorite())
                favoriteButton.setBackgroundResource(R.drawable.ic_is_favorite);
            else
                favoriteButton.setBackgroundResource(R.drawable.ic_not_favorite);

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
