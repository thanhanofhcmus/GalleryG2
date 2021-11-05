package com.gnine.galleryg2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.tools.SliderItem;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private ArrayList<ImageData> imageDataList;
    private ViewPager2 viewPager2;



    public SliderAdapter( ArrayList<ImageData> imageDataList, ViewPager2 viewPager2) {

        this.imageDataList = imageDataList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
//        holder.setImage(sliderItems.get(position));
        Glide.with(holder.imageView.getContext())
                .load(imageDataList.get(position).uri)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }



    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

//        void setImage(SliderItem sliderItem) {
//            imageView.setImageResource(sliderItem.getImage());
//        }
    }
}
