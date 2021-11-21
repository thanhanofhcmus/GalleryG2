package com.gnine.galleryg2.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.TrashData;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;

public class TrashAdapter extends
        RecyclerView.Adapter<TrashAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public View scrim, check;

        ImageViewHolder(@NonNull View view,
                        BiConsumer<Integer, View> onItemClick,
                        BiConsumer<Integer, View> onItemLongClick) {
            super(view);
            imageView = view.findViewById(R.id.pictureItemImageView);
            scrim = itemView.findViewById(R.id.pictureItemScrim);
            check = itemView.findViewById(R.id.pictureItemCheck);

            itemView.setOnLongClickListener(view1 -> {
                onItemLongClick.accept(getAbsoluteAdapterPosition(), view1);
                return false;
            });

            itemView.setOnClickListener(
                    view1 -> onItemClick.accept(getAbsoluteAdapterPosition(), view1));
        }
    }

    public enum State {
        Normal,
        MultipleSelect
    }

    @NonNull
    private final List<TrashData> imageDataList;
    @NonNull
    private final BiConsumer<Integer, View> onItemClick;
    @NonNull
    private final BiConsumer<Integer, View> onItemLongClick;

    private TrashAdapter.State state = TrashAdapter.State.Normal;

    public TrashAdapter(@NonNull List<TrashData> imageDataList,
                        @NonNull BiConsumer<Integer, View> onItemClick,
                        @NonNull BiConsumer<Integer, View> onItemLongClick) {
        this.imageDataList = imageDataList;
        this.onItemClick = onItemClick;
        this.onItemLongClick = onItemLongClick;
    }

    @NonNull
    @Override
    public TrashAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.picture_item, parent, false);
        return new ImageViewHolder(view, onItemClick, onItemLongClick);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashAdapter.ImageViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        TrashData imageData = imageDataList.get(position);

        int index = imageData.oldPath.lastIndexOf("/");
        String name = imageData.oldPath.substring(index + 1);
        String trashpath = imageData.trashPath + "/" + name;
        Uri uri = Uri.fromFile(new File(trashpath));

        Glide.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.bird_thumbnail)
                .transform(new CenterCrop(), new RoundedCorners(24))
                .into(imageView);
        if (state == TrashAdapter.State.Normal) {
            holder.scrim.setVisibility(View.GONE);
            holder.check.setVisibility(View.GONE);
            imageData.setChecked(false);
        } else {
            if (imageData.isChecked()) {
                holder.scrim.setVisibility(View.VISIBLE);
                holder.check.setVisibility(View.VISIBLE);
            } else {
                holder.scrim.setVisibility(View.GONE);
                holder.check.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public TrashAdapter.State getState() {
        return state;
    }

    public void setState(TrashAdapter.State state) {
        this.state = state;
    }
}
