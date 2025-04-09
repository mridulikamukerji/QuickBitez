package com.example.quickbitez;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderItem> sliderItems;

    public SliderAdapter(List<SliderItem> sliderItems) {
        this.sliderItems = sliderItems;
    }

    public void addItem(SliderItem item) {
        sliderItems.add(item);
        notifyItemInserted(sliderItems.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < sliderItems.size()) {
            sliderItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false); // Your layout file
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderItem sliderItem = sliderItems.get(position);
        Bitmap imageBitmap = sliderItem.getImageBitmap(); // updated for Bitmap
        holder.imageView.setImageBitmap(imageBitmap);     // updated method
        holder.nameTextView.setText(sliderItem.getName());
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_image);
            nameTextView = itemView.findViewById(R.id.slider_name);
        }
    }
}
