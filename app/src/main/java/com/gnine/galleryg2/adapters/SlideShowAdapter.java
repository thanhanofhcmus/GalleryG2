package com.gnine.galleryg2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SlideShowAdapter extends SliderViewAdapter<SlideShowAdapter.Holder> {

    private ArrayList<ImageData> imageDataList;

    public SlideShowAdapter(ArrayList<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slide_image);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.slideshow_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        Glide.with(viewHolder.imageView.getContext())
                .load(imageDataList.get(position).uri)
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return imageDataList.size();
    }
}
