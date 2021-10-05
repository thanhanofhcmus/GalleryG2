package com.gnine.galleryg2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        ImageViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.pictureItemImageView);
        }
    }

    final private int[] resourceIds;

    public ImageRecyclerViewAdapter(int[] resourceIds) {
        this.resourceIds = resourceIds;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.picture_item, parent, false);
//        view.getLayoutParams().height = parent.getLayoutParams().width / 3;
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        imageView.setImageResource(resourceIds[position]);
    }

    @Override
    public int getItemCount() {
        return resourceIds.length;
    }
}
