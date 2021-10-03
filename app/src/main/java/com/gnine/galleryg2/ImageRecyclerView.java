package com.gnine.galleryg2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageRecyclerView extends RecyclerView.Adapter<ImageRecyclerView.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageViewHolder(@NonNull View view) {
            super(view);
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.picture_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 25;
    }
}
