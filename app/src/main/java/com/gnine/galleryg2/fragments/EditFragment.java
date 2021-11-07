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

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gnine.galleryg2.FullImageActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;

import java.io.File;
import java.io.FileOutputStream;

public class EditFragment extends Fragment {

    private int angle;
    private ImageData imageData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        ImageView imageView = view.findViewById(R.id.singleImage);

        assert getArguments() !=null;
        imageData=(ImageData) getArguments().getParcelable(FullImageActivity.IMAGE_DATA_KEY);
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
//                v ->Navigation.findNavController(view).navigate(R.id.editToRotateFragment)
        v -> {
            final RotateAnimation rotateAnim = new RotateAnimation(
                    angle, angle + 90, RotateAnimation.RELATIVE_TO_SELF, .5f,
                    RotateAnimation.RELATIVE_TO_SELF, .5f);
            rotateAnim.setDuration(1000); // Use 0 ms to rotate instantly
            rotateAnim.setFillAfter(true); // Must be true or the animation will reset
            imageView.startAnimation(rotateAnim);
            angle += 90;
        });
        assert getActivity() != null;
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Bitmap rotate(Bitmap source, int angle){
        Matrix matrix =new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,false);
    }

    private void saveImage(ImageView imageView){
        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        bitmap=rotate(bitmap,angle);
        File file=Environment.getExternalStorageDirectory();
        File dir=new File(file.getAbsolutePath());
        dir.mkdirs();
        File outFile=new File(dir,imageData.name);
//        if(file.exists()) {
//            System.out.println("exists");
//            file.delete();
//        }
        try{
            FileOutputStream fos=new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}