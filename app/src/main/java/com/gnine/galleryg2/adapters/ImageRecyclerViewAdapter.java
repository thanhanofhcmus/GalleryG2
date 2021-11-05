package com.gnine.galleryg2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.R;

import java.util.List;

public class ImageRecyclerViewAdapter extends
        RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public View scrim;
        public CheckBox checkBox;

        ImageViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.pictureItemImageView);
            scrim = itemView.findViewById(R.id.pictureItemScrim);
            checkBox = itemView.findViewById(R.id.pictureItemCheckCircle);

            itemView.setOnLongClickListener(view1 -> {
                onItemLongClickListener.onItemLongClick(getAdapterPosition(), view1);
                return false;
            });

            itemView.setOnClickListener(
                    view1 -> onItemClickListener.onItemClick(getAdapterPosition(), view1));
        }
    }

    @NonNull final private List<ImageData> imageDataList;
    private int ACTION_MODE = 0;
    private static OnItemLongClickListener onItemLongClickListener = null;
    private static OnItemClickListener onItemClickListener = null;

    public ImageRecyclerViewAdapter(@NonNull List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
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
        ImageData imageData = imageDataList.get(position);
        Glide.with(imageView.getContext())
                .load(imageData.uri)
                .placeholder(R.drawable.bird_thumbnail)
                .into(imageView);
        if (ACTION_MODE == 0) {
            holder.scrim.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.GONE);
            imageData.setChecked(false);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.scrim.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(imageData.isChecked());
        }
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public int getACTION_MODE() {
        return ACTION_MODE;
    }

    public void setACTION_MODE(int ACTION_MODE) {
        this.ACTION_MODE = ACTION_MODE;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        ImageRecyclerViewAdapter.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        ImageRecyclerViewAdapter.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
