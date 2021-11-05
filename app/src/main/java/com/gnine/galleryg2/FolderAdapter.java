package com.gnine.galleryg2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.fragments.AllImagesFragment;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    public class FolderViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title;
        private TextView count;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFolderClick.onClick(v, getAbsoluteAdapterPosition());
                }
            });
        }
    }

    private List<Folder> folderList;
    private static FolderClick onFolderClick = null;

    public void setData(List<Folder> list) {
        this.folderList = list;
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
        Folder folder = folderList.get(position);
        if (folder == null)
            return;
        holder.icon.setImageResource(folder.getResourceId());
        holder.title.setText(String.valueOf(folder.getTitle()));
        if (folder.getImageList() != null) {
            holder.count.setText(Integer.toString(folder.getImageList().size()));
        }
        else {
            holder.count.setText(Integer.toString(0));
        }
    }

    @Override
    public int getItemCount() {
        if (folderList != null)
            return folderList.size();
        return 0;
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<Folder> folderList) {
        this.folderList = folderList;
    }

    public static FolderClick getOnFolderClick() {
        return onFolderClick;
    }

    public static void setOnFolderClick(FolderClick onFolderClick) {
        FolderAdapter.onFolderClick = onFolderClick;
    }

    public interface FolderClick {
        void onClick(View view, int position);
    }
}
