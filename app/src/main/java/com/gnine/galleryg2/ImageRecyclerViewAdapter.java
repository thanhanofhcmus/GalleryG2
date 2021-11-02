package com.gnine.galleryg2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.stream.Collectors;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder>{

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public View scrim;
        public CheckBox checkBox;

        ImageViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.pictureItemImageView);
            scrim = (View) itemView.findViewById(R.id.pictureItemScrim);
            checkBox = (CheckBox) itemView.findViewById(R.id.pictureItemCheckCircle);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickListener.onItemLongClick(getAdapterPosition(), view);
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }

    final private int[] resourceIds;
    final private List<ImageData> imageDataList;
    private int ACTION_MODE = 0;
    private static OnItemLongClickListener onItemLongClickListener = null;
    private static OnItemClickListener onItemClickListener = null;

    public ImageRecyclerViewAdapter(int[] resourceIds) {
        this.resourceIds = resourceIds;
        this.imageDataList = null;
    }

    public ImageRecyclerViewAdapter(List<ImageData> imageDataList) {
        this.resourceIds = null;
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
        if (imageDataList != null ) {
            ImageData imageData = imageDataList.get(position);
            Glide.with(imageView.getContext())
                    .load(imageData.uri)
                    .placeholder(R.drawable.bird_thumbnail)
                    .into(imageView);
            if (ACTION_MODE == 0) {
                holder.scrim.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.GONE);
                imageData.setChecked(false);
            }
            else {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.scrim.setVisibility(View.VISIBLE);
                if (imageData.isChecked()) {
                    holder.checkBox.setChecked(true);
                }
                else {
                    holder.checkBox.setChecked(false);
                }
            }
        } else {
            assert resourceIds != null;
            imageView.setImageResource(resourceIds[position]);
        }
    }

    @Override
    public int getItemCount() {
        if (imageDataList != null ) {
            return imageDataList.size();
        } else {
            assert resourceIds != null;
            return resourceIds.length;
        }
    }

    public int getACTION_MODE() {
        return ACTION_MODE;
    }

    public void setACTION_MODE(int ACTION_MODE) {
        this.ACTION_MODE = ACTION_MODE;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = (OnItemLongClickListener) onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
