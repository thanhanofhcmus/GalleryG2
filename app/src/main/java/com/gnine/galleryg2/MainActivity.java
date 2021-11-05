package com.gnine.galleryg2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.gnine.galleryg2.data.ImageData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ISendImageListListener{

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 111;
    private ArrayList<ImageData> imageDataList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.mainAppbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            setupBottomNavigation();
        }
//        imageDataList = ImageLoader.loadImageFromSharedStorage(this);
//        allImagesFragment=(AllImagesFragment) getSupportFragmentManager().findFragmentById(R.id.allImagesFragment);
//        allImagesFragment.sendImageList(imageDataList);
//        iSendImageListListener.sendImageList(imageDataList);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupBottomNavigation();
        }
    }

    private void setupBottomNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    @Override
    public void sendImageList(ArrayList<ImageData> list) {
        imageDataList=list;
    }

    @Override
    public void sendImagePosition(int position) {
        Intent intent=new Intent(MainActivity.this, FullImageActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("image list", (Serializable) imageDataList);
        intent.putExtra("image list",bundle);
        intent.putExtra("position",position);

        startActivity(intent);
    }
}
