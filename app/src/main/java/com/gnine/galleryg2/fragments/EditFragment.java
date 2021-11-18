package com.gnine.galleryg2.fragments;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gnine.galleryg2.FullImageActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditFragment extends Fragment {

    private int angle;
    private ImageData imageData;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        assert getArguments() !=null;
        imageData=(ImageData) getArguments().getParcelable(FullImageActivity.IMAGE_DATA_KEY);
        bitmap=(Bitmap) getArguments().getParcelable(FullImageActivity.BITMAP_DATA_KEY);
//        {
//            final RotateAnimation rotateAnim = new RotateAnimation(
//                    angle, angle + 90, RotateAnimation.RELATIVE_TO_SELF, .5f,
//                    RotateAnimation.RELATIVE_TO_SELF, .5f);
//            rotateAnim.setDuration(1000); // Use 0 ms to rotate instantly
//            rotateAnim.setFillAfter(true); // Must be true or the animation will reset
//            imageView.startAnimation(rotateAnim);
//            angle += 90;
//        }
//        );


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.singleImage);
        Glide.with(imageView.getContext())
                .load(imageData.uri)
                .placeholder(R.drawable.bird_thumbnail)
                .into(imageView);
        view.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            assert getActivity() !=null;
            getActivity().onBackPressed();
        });
        view.findViewById(R.id.saveBtn).setOnClickListener(
                v -> saveImage(imageView)
        );
        angle=0;
        view.findViewById(R.id.rotateBtn).setOnClickListener(
                v ->{
                    Bundle bundle=new Bundle();
                    bundle.putParcelable(FullImageActivity.IMAGE_DATA_KEY,imageData);
                    Navigation.findNavController(view).navigate(R.id.editToRotateFragment,bundle);
                });
        view.findViewById(R.id.cropBtn).setOnClickListener(
                v-> Navigation.findNavController(view).navigate(R.id.editToCropFragment));
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert  actionBar != null;
        actionBar.hide();
    }


    private void saveImage(ImageView imageView){
        assert bitmap!=null;
//        String path=Environment.getExternalStorageDirectory().toString();
        File path= (new File(imageData.uri.getPath())).getParentFile();
        //String path_=imageData.uri.toString();
        FileOutputStream fout=null;
        File file=new File(path,"1.jpg");
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch(Exception ignored) {

            }
        }
        try {
            //cannot write data
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,fout);
            fout.flush();
            fout.close();
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}