package com.example.fuelbee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.cardViewHolder> {
    private List<CardItem> imageList;

    public CardAdapter(List<CardItem> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new cardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        CardItem cardItem = imageList.get(position);
        holder.cardImage.setImageResource(cardItem.getCardImage());
        holder.cardTitle.setText(cardItem.getCardTitle());
        holder.cardDistance.setText(cardItem.getCardDistance());

        holder.itemView.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) v.getContext();
            ProductDetail fragment = ProductDetail.newInstance(
                    cardItem.getCardImage(),
                    cardItem.getCardTitle(),
                    cardItem.getCardDistance()
            );
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class cardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardTitle;
        TextView cardDistance;

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.petrolPump1);
            cardTitle = itemView.findViewById(R.id.Title);
            cardDistance = itemView.findViewById(R.id.Distance);
        }
    }
}
