package com.gnine.galleryg2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.R;

import java.util.List;
import java.util.function.BiConsumer;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    public static class FolderViewHolder extends RecyclerView.ViewHolder {

        public final ImageView icon;
        public final TextView title;
        public final TextView count;
        public final View scrim, cancel;

        public FolderViewHolder(@NonNull View itemView, BiConsumer<Integer, View> onFolderClick) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
            scrim = itemView.findViewById(R.id.folderScrim);
            cancel = itemView.findViewById(R.id.folderCancel);

            itemView.setOnClickListener(view -> onFolderClick.accept(getAbsoluteAdapterPosition(), view));
        }
    }

    public enum State {
        Normal,
        Edit
    }

    private List<FolderData> mFolderDataList;
    private final BiConsumer<Integer, View> onFolderClick;
    private State state = State.Normal;

    public FolderAdapter(List<FolderData> folderDataList,
                         BiConsumer<Integer, View> onFolderClick) {
        mFolderDataList = folderDataList;
        this.onFolderClick = onFolderClick;
    }

    public void setData(List<FolderData> list) {
        this.mFolderDataList = list;
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(view, onFolderClick);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderData folderData = mFolderDataList.get(position);
        if (folderData == null) {
            return;
        }
        holder.icon.setImageResource(folderData.resourceId);
        holder.title.setText(String.valueOf(folderData.title));

        List<ImageData> imageDataList = folderData.imageList;
        String count = imageDataList != null ? String.valueOf(imageDataList.size()) : "0";
        holder.count.setText(count);

        if (state == State.Normal) {
            holder.scrim.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
        } else {
            holder.scrim.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mFolderDataList != null ? mFolderDataList.size() : 0;
    }

    public State getState() {return state;}

    public void setState(State state) {this.state = state;}
}
