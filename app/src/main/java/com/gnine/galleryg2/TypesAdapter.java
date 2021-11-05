package com.gnine.galleryg2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.TypesViewHolder> {

    private List<Types> typesList;

    public void setData(List<Types> list) {
        this.typesList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TypesAdapter.TypesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.types_item, parent, false);
        return new TypesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypesViewHolder holder, int position) {
        Types types = typesList.get(position);
        if (types == null)
            return;
        holder.icon.setImageResource(types.getResrcId());
        holder.title.setText(String.valueOf(types.getTitle()));
        if (types.getList() != null) {
            holder.count.setText(Integer.toString(types.getList().size()));
        }
        else {
            holder.count.setText(Integer.toString(0));
        }
    }

    @Override
    public int getItemCount() {
        if (typesList != null)
            return typesList.size();
        return 0;
    }

    public class TypesViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title;
        private TextView count;

        public TypesViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
        }
    }
}
