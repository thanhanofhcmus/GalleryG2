package com.gnine.galleryg2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.TimelineData;

import java.util.List;
import java.util.function.BiConsumer;

public class ImageRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public static class TimelineViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        TimelineViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.timelineItemText);
        }
    }

    public enum State {
        Normal,
        MultipleSelect
    }
    public static int ITEM_TYPE_TIME = 0;
    public static int ITEM_TYPE_IMAGE = 1;
    @NonNull
    private final List<TimelineData> imageDataList;
    @NonNull
    private final BiConsumer<Integer, View> onItemClick;
    @NonNull
    private final BiConsumer<Integer, View> onItemLongClick;

    private State state = State.Normal;

    public ImageRecyclerViewAdapter(@NonNull List<TimelineData> imageDataList,
                                    @NonNull BiConsumer<Integer, View> onItemClick,
                                    @NonNull BiConsumer<Integer, View> onItemLongClick) {
        this.imageDataList = imageDataList;
        this.onItemClick = onItemClick;
        this.onItemLongClick = onItemLongClick;
    }

    @Override
    public int getItemViewType(int position) {
        if (imageDataList.get(position).type == TimelineData.Type.Time) {
            return ITEM_TYPE_TIME;
        } else {
            return ITEM_TYPE_IMAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_IMAGE) {
            View view = inflater.inflate(R.layout.picture_item, parent, false);
            return new ImageViewHolder(view, onItemClick, onItemLongClick);
        } else {
            return new TimelineViewHolder(inflater.inflate(R.layout.timeline_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (imageDataList.get(position).type == TimelineData.Type.Time) {
            final TimelineViewHolder timelineHolder = (TimelineViewHolder) holder;
            //long timelineData = imageDataList.get(position).time;
            timelineHolder.textView.setText(imageDataList.get(position).time);
        } else {
            final ImageViewHolder imageHolder = (ImageViewHolder) holder;
            ImageView imageView = imageHolder.imageView;
            ImageData imageData = imageDataList.get(position).imageData;
            Glide.with(imageView.getContext())
                    .load(imageData.uri)
                    .placeholder(R.drawable.bird_thumbnail)
                    .transform(new CenterCrop(), new RoundedCorners(24))
                    .into(imageView);
            if (state == State.Normal) {
                imageHolder.scrim.setVisibility(View.GONE);
                imageHolder.check.setVisibility(View.GONE);
                imageData.setChecked(false);
            } else {
                if (imageData.isChecked()) {
                    imageHolder.scrim.setVisibility(View.VISIBLE);
                    imageHolder.check.setVisibility(View.VISIBLE);
                } else {
                    imageHolder.scrim.setVisibility(View.GONE);
                    imageHolder.check.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
