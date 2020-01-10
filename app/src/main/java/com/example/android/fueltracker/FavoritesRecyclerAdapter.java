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

public class FavoritesRecyclerAdapter extends RecyclerView.Adapter<FavoritesRecyclerAdapter.FavoritesViewHolder>
{

    private Context context;
    private ArrayList<Favorites> favorites;
    private FavoritesRecyclerAdapter.ListItemClickListener listener;
    private TextView favoriteName;
    private TextView favoriteAddress;
    private TextView favoriteCity;
    private ImageView favoriteLogo;
    private LinearLayout favoriteNameAddress;

    public interface ListItemClickListener
    {
        void AddressClick(int position);
    }

    public FavoritesRecyclerAdapter(FavoritesRecyclerAdapter.ListItemClickListener listener, ArrayList<Favorites> favorites)
    {
        this.listener = listener;
        this.favorites = favorites;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public FavoritesRecyclerAdapter.FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.favorites_list, parent, false);
        FavoritesRecyclerAdapter.FavoritesViewHolder viewHolder = new FavoritesRecyclerAdapter.FavoritesViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) { return Integer.parseInt(favorites.get(position).getId()); }

    @Override
    public int getItemViewType(int position) { return Integer.parseInt(favorites.get(position).getId()); }

    @Override
    public void onBindViewHolder(@NonNull FavoritesRecyclerAdapter.FavoritesViewHolder holder, int position) { holder.bind(position); }

    @Override
    public int getItemCount() { return favorites.size(); }

    class FavoritesViewHolder extends RecyclerView.ViewHolder
    {

        public FavoritesViewHolder(@NonNull View itemView)
        {
            super(itemView);
            favoriteName = itemView.findViewById(R.id.favoriteName);
            favoriteAddress = itemView.findViewById(R.id.favoriteAddress);
            favoriteCity = itemView.findViewById(R.id.favoriteCity);
            favoriteLogo = itemView.findViewById(R.id.favoriteLogo);
            favoriteNameAddress = itemView.findViewById(R.id.favoriteNameAddress);

            favoriteNameAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int clickedPosition = getAdapterPosition();
                    listener.AddressClick(clickedPosition);
                }
            });
        }

        public void bind(int position)
        {
            favoriteName.setText(favorites.get(position).getName());
            favoriteAddress.setText(favorites.get(position).getAddress());
            favoriteCity.setText(favorites.get(position).getCity());

            switch (favorites.get(position).getName())
            {
                case "Arco": favoriteLogo.setImageResource(R.drawable.arco); break;
                case "Mobil": favoriteLogo.setImageResource(R.drawable.mobil); break;
                case "Shell": favoriteLogo.setImageResource(R.drawable.shell); break;
                case "Chevron": favoriteLogo.setImageResource(R.drawable.chevron); break;
                case "76": favoriteLogo.setImageResource(R.drawable.seventysix); break;
                case "Valero": favoriteLogo.setImageResource(R.drawable.valero); break;
                case "Texaco": favoriteLogo.setImageResource(R.drawable.texaco); break;
                case "Exxon": favoriteLogo.setImageResource(R.drawable.exxon); break;
                case "7-Eleven": favoriteLogo.setImageResource(R.drawable.seveneleven); break;
                case "Sinclair": favoriteLogo.setImageResource(R.drawable.sinclair); break;
                default: favoriteLogo.setImageResource(R.drawable.generic); break;
            }
        }

    }
}
