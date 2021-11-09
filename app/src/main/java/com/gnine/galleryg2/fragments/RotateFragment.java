package com.gnine.galleryg2.fragments;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gnine.galleryg2.FullImageActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;

public class RotateFragment extends Fragment {


    private ImageData imageData;
    private int angle;

    public RotateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rotate, container, false);
        assert getArguments() !=null;
        imageData=(ImageData) getArguments().getParcelable(FullImageActivity.IMAGE_DATA_KEY);
        angle=0;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.singleImage_rotate);
        Glide.with(imageView.getContext())
                .load(imageData.uri)
                .placeholder(R.drawable.bird_thumbnail)
                .into(imageView);
        view.findViewById(R.id.leftRotateBtn).setOnClickListener(v-> angle-=90);
        view.findViewById(R.id.rightRotateBtn).setOnClickListener(v-> angle+=90);
        view.findViewById(R.id.closeBtn_rotate).setOnClickListener(
                v ->{
                    assert getActivity() !=null;
                    getActivity().onBackPressed();
                });
        view.findViewById(R.id.saveBtn_rotate).setOnClickListener(v-> {
            Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            bitmap=rotateBitmap(bitmap,angle);
            Bundle bundle=new Bundle();
            bundle.putParcelable(FullImageActivity.BITMAP_DATA_KEY,bitmap);
            bundle.putParcelable(FullImageActivity.IMAGE_DATA_KEY,imageData);
            Navigation.findNavController(view).navigate(R.id.rotateToEditFragment,bundle);
        });
    }

    private Bitmap rotateBitmap(Bitmap source, int angle){
        Matrix matrix =new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,false);
    }
}