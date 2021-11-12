package com.gnine.galleryg2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.tools.UCrop;

import java.io.File;
import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    public static final String IMAGE_DATA_KEY= "IMAGE_DATA_KEY";
    public static final String BITMAP_DATA_KEY="BITMAP_DATA_KEY";
    public static final String IMAGE_DATA="IMAGE_DATA";
    private static final String SAMPLE_CROP_IMAGE_NAME="SampleCropImage";
    private ArrayList<ImageData> imageDataList;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
        findViewById(R.id.editBtn).setOnClickListener(v->{
            getData();
            startCropActivity();
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_information) {
            ViewPager2 viewPager2=findViewById(R.id.viewPagerImageSlider);
            int position=viewPager2.getCurrentItem();
            Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_aboutUs) {
            Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
        String a=getCacheDir().getAbsolutePath();
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