package com.gnine.galleryg2.fragments;

import android.Manifest;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.PreferenceFragmentCompat;

import com.gnine.galleryg2.activities.FullImageActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.tools.PermissionChecker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wallpaper.WallpaperApplyTask;

import java.io.IOException;

public class InformationFragment extends PreferenceFragmentCompat {

    ActivityResultLauncher<String> wallpaperResultLauncher;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.information_layout, rootKey);
        ImageData imageData = FullImageActivity.getImageData();

        wallpaperResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> setWallpaper(imageData.uri)
        );

        try {
            findPreference("Name").setSummary(imageData.name);
            findPreference("Path").setSummary(imageData.uri.getPath());
            findPreference("Size").setSummary(imageData.size + " bytes");
            findPreference("Date").setSummary(imageData.getDateTime());

            findPreference("Wallpaper").setOnPreferenceClickListener(preference -> {
                String permission = Manifest.permission.SET_WALLPAPER;
                if (! PermissionChecker.checkPermission(this, permission)) {
                    wallpaperResultLauncher.launch(permission);
                } else {
                    setWallpaper(imageData.uri);
                }
                return true;
            });
        } catch (Exception e) {
            Log.e("Error", "onCreatePreferences: " + e.getMessage());
        }

        FullImageActivity.setIsInViewpagerFragment(false);
    }

    private void setWallpaper(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            WallpaperManager.getInstance(getContext()).setBitmap(bitmap);
        } catch (IOException e) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error has occurred")
                    .setPositiveButton("GOT IT", null)
                    .show();
        }
    }
}