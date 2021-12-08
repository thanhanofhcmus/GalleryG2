package com.gnine.galleryg2.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class FullImageActivity extends AppCompatActivity {

    private static ImageData imageData = null;
    private static boolean isInViewpagerFragment;
    private static int currentImagePosition = 0;

    private static final String SAMPLE_CROP_IMAGE_NAME = "SampleCropImage";

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

    public void startCrop(@NonNull Uri uri) {
        String sourceName = uri.toString();
        int dotPos = sourceName.lastIndexOf('.');
        String desc = sourceName.substring(0, dotPos) + "_copy" + sourceName.substring(dotPos);

        UCrop.of(uri, Uri.parse(desc))
                .withAspectRatio(16, 9)
                .withMaxResultSize(1000, 1000)
                .start(this);
    }
}
