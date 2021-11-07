package com.gnine.galleryg2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.data.TypeData;

import java.util.List;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.TypesViewHolder> {

    private List<TypeData> mTypeDataList;

    public void setData(List<TypeData> list) {
        this.mTypeDataList = list;
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    @Override
    public TypesAdapter.TypesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.types_item, parent, false);
        return new TypesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypesViewHolder holder, int position) {
        TypeData typeData = mTypeDataList.get(position);
        if (typeData == null) {
            return;
        }
        holder.icon.setImageResource(typeData.resourceId);
        holder.title.setText(String.valueOf(typeData.title));

       List<ImageData> typeDataList = typeData.list;
        String count = typeDataList != null ? String.valueOf(typeDataList.size()) : "0";
        holder.count.setText(count);
    }

    @Override
    public int getItemCount() {
        if (mTypeDataList != null)
            return mTypeDataList.size();
        return 0;
    }

    public static class TypesViewHolder extends RecyclerView.ViewHolder {

        private final ImageView icon;
        private final TextView title;
        private final TextView count;

        public TypesViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
        }
    }
}
