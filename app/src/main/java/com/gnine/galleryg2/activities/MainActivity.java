package com.gnine.galleryg2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.gnine.galleryg2.tools.PermissionChecker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGE_LIST_KEY = "IMAGE_LIST_KEY";
    public static final String IMAGE_POSITION_KEY = "IMAGE_POSITION_KEY";
    public static final String IS_ALBUM = "IS_ALBUM";
    public static final String ALBUM_TITLE = "ALBUM_TITLE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.mainAppbar);
        setSupportActionBar(toolbar);

        LocalDataManager.init(getApplicationContext());

        final String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (! PermissionChecker.checkPermission(this, permission)) {
            PermissionChecker.requestPermission(this, permission, granted -> { if (granted) { setupBottomNavigation(); } });
        } else {
            setupBottomNavigation();
        }
    }

    private void setupBottomNavigation() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setNightMode(preferences.getString("theme_mode", "invalid"));

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void invokeFullImageActivity(ArrayList<ImageData> imageDataList, int position, boolean isAlbum, String albumTitle) {
        Intent intent = new Intent(MainActivity.this, FullImageActivity.class);
        intent.putExtra(IMAGE_LIST_KEY, imageDataList);
        intent.putExtra(IMAGE_POSITION_KEY, position);
        intent.putExtra(IS_ALBUM, isAlbum);
        intent.putExtra(ALBUM_TITLE, albumTitle);

        startActivity(intent);
    }

    public static void setNightMode(String value) {
        if (value.equals("Light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (value.equals("Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
