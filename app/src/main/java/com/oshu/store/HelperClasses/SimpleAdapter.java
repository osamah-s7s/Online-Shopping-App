package com.oshu.store.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.oshu.store.R;

import java.util.ArrayList;
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.FeaturedViewHolder> {
    ArrayList<SimpleHelperClass> featuredLocations;
    private SelectListner selectListener;

    public SimpleAdapter(ArrayList<SimpleHelperClass> featuredLocations, SelectListner selectListener) {
        this.featuredLocations = featuredLocations;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        SimpleHelperClass simpleHelperClass = featuredLocations.get(position);

        // Bind data to ViewHolder
        holder.image.setImageResource(simpleHelperClass.getImage());
        holder.title.setText(simpleHelperClass.getTitle());
        holder.description.setText(simpleHelperClass.getDescription());

        // Set click listener on the item
        holder.itemView.setOnClickListener(view -> {
            if (selectListener != null) {
                selectListener.onItemClick(simpleHelperClass);
            }
        });
    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialization
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_desc);
        }
    }
}
