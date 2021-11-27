package com.gnine.galleryg2.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private final ArrayList<ImageData> imageDataList;

    public SliderAdapter(ArrayList<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.fragment_image_view,parent,false);
        ImageView imageView=view.findViewById(R.id.imageSlide);
        Zoomy.Builder builder=new Zoomy.Builder((Activity) view.getContext())
                .target(imageView)
                .animateZooming(false)
                .enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {

                    }
                });
        builder.register();
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(imageDataList.get(position).uri)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;


        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
    }
}
