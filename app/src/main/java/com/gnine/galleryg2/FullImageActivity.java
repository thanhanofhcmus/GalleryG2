package com.gnine.galleryg2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gnine.galleryg2.data.ImageData;

public class FullImageActivity extends AppCompatActivity {

    private static ImageData imageData = null;
    private static boolean isInViewpagerFragment;

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