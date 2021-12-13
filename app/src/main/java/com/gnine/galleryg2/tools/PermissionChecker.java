package com.gnine.galleryg2.tools;

import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class PermissionChecker {
    public static boolean checkPermission(Fragment fragment, String permission) {
        return ActivityCompat.checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermission(AppCompatActivity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(AppCompatActivity activity, String permission, ActivityResultCallback<Boolean> callback) {
        activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), callback).launch(permission);
    }
}
