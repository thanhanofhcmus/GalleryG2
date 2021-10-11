package com.gnine.galleryg2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        ImageViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.pictureItemImageView);
        }
    }

    final private int[] resourceIds;
    final private List<Uri> imageUris;

    public ImageRecyclerViewAdapter(int[] resourceIds) {
        this.resourceIds = resourceIds;
        this.imageUris = null;
    }

    public ImageRecyclerViewAdapter(List<Uri> imageUris) {
        this.resourceIds = null;
        this.imageUris = imageUris;
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
        ImageView imageView = holder.imageView;
        if (imageUris != null ) {
            Uri uri = imageUris.get(position);
            Glide.with(imageView.getContext())
                    .load(uri)
                    .into(imageView);
        } else {
            assert resourceIds != null;
            imageView.setImageResource(resourceIds[position]);
        }
    }

    @Override
    public int getItemCount() {
        if (imageUris != null ) {
            return imageUris.size();
        } else {
            assert resourceIds != null;
            return resourceIds.length;
        }
    }
}
