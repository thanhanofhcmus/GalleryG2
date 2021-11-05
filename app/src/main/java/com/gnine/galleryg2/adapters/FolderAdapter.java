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

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    public static class FolderViewHolder extends RecyclerView.ViewHolder {

        public final ImageView icon;
        public final TextView title;
        public final TextView count;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);

//            itemView.setOnClickListener(v -> onFolderClick.onClick(v, getAbsoluteAdapterPosition()));
        }
    }

    private List<FolderData> mFolderDataList;
    private static FolderClick onFolderClick = null;

    public void setData(List<FolderData> list) {
        this.mFolderDataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return mFolderDataList != null ? mFolderDataList.size() : 0;
    }

    public static void setOnFolderClick(FolderClick onFolderClick) {
        FolderAdapter.onFolderClick = onFolderClick;
    }

    public interface FolderClick {
        void onClick(View view, int position);
    }
}
