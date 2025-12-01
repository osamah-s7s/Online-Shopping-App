package com.oshu.store.HelperClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oshu.store.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    private ArrayList<FeaturedHelperClass> featuredLocations;
    private Context context;
    private SelectListner selectListner;
    private OnItemClickListener onItemClickListener;

    public FeaturedAdapter(ArrayList<FeaturedHelperClass> featuredLocations, Context context, SelectListner selectListner) {
        this.featuredLocations = featuredLocations;
        this.context = context;
        this.selectListner = selectListner; // Corrected variable name
    }


    public interface OnItemClickListener {
        void onItemClick(FeaturedHelperClass item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        FeaturedHelperClass featuredHelperClass = featuredLocations.get(position);

        holder.title.setText(featuredHelperClass.getTitle());
        holder.description.setText(featuredHelperClass.getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectListner.onitemClick(featuredLocations.get(position));
            }
        });

        // Load image using Glide
        Glide.with(context)
                .load(featuredHelperClass.getImage()) // Assuming getImage returns a valid URL or URI
                .placeholder(R.drawable.default_product_image)
                .into(holder.image);

        // Set click listener on the item
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(featuredHelperClass);
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
        ImageView cardView;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialization
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_desc);
            cardView = itemView.findViewById(R.id.featured_image); // Use the correct ID
        }
    }
}
