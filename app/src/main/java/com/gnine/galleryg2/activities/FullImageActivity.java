package com.gnine.galleryg2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;


public class FullImageActivity extends AppCompatActivity {

    private static ImageData imageData = null;
    private static boolean isInViewpagerFragment;
    private static int currentImagePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        currentImagePosition = getIntent().getExtras().getInt(MainActivity.IMAGE_POSITION_KEY);

        Toolbar toolbar = findViewById(R.id.fullImageToolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            if (isInViewpagerFragment) {
                finish();
            } else {
                onBackPressed();
            }
        });

        setTitle("GalleryG2");
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

    public static void setCurrentImagePosition(int position) {
        currentImagePosition = position;
    }

    public static int getCurrentImagePosition() {
        return currentImagePosition;
    }

    public void startEdit(@NonNull Uri uri) {
        String IMAGE_SOURCE = "IMAGE_SOURCE";
        Intent intent = new Intent(FullImageActivity.this, EditPhotoActivity.class);
        intent.putExtra(IMAGE_SOURCE, uri.getPath());

        startActivity(intent);
    }


}
