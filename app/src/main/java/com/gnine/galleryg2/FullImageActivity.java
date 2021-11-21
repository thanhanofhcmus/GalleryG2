package com.gnine.galleryg2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.tools.UCrop;

public class FullImageActivity extends AppCompatActivity {

    private static ImageData imageData = null;
    private static boolean isInViewpagerFragment;

import java.io.File;
import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    public static final String IMAGE_DATA_KEY= "IMAGE_DATA_KEY";
    public static final String BITMAP_DATA_KEY="BITMAP_DATA_KEY";
    public static final String IMAGE_DATA="IMAGE_DATA";
    private static final String SAMPLE_CROP_IMAGE_NAME="SampleCropImage";
    private ArrayList<ImageData> imageDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toolbar toolbar = findViewById(R.id.fullImageToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(view -> {
            if (isInViewpagerFragment) {
                finish();
            } else {
                onBackPressed();
            }
        }
        findViewById(R.id.editBtn).setOnClickListener(v->{
            getData();
            startCropActivity();
        });
    }

    public static void setImageData(ImageData data) {
        imageData = data;
    }

    public static ImageData getImageData() {
        return imageData;
    }

    public static void setIsInViewpagerFragment(boolean value) {
        isInViewpagerFragment = value;
    }
}
    private void getData(){
        Bundle bundle= getIntent().getExtras();
        imageDataList = bundle.getParcelableArrayList(MainActivity.IMAGE_LIST_KEY);
    }

    public void invokeUCropActivity(ImageData imageData){
        Intent intent=new Intent(FullImageActivity.this,UCropActivity.class);
        intent.putExtra(IMAGE_DATA,imageData);
        startActivity(intent);
    }

    private void startCropActivity(){
        ViewPager2 viewPager2=findViewById(R.id.viewPagerImageSlider);
        int position=viewPager2.getCurrentItem();
        Uri uri=imageDataList.get(position).uri;
        startCrop(uri);
    }

    private void startCrop(@NonNull Uri uri){
        String destinationName=SAMPLE_CROP_IMAGE_NAME+".jpg";
        UCrop uCrop=UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationName)));
        uCrop=uCrop.useSourceImageAspectRatio();
        UCrop.Options options=new UCrop.Options();
        options.setCompressionQuality(90);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        uCrop=uCrop.withOptions(options);
        uCrop.start(FullImageActivity.this);
    }
}
