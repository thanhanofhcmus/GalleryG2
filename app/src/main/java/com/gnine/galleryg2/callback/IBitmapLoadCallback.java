package com.gnine.galleryg2.callback;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gnine.galleryg2.model.ExifInfor;

public interface IBitmapLoadCallback {

    void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfor exifInfor, @NonNull String imageInputPath, @Nullable String imageOutputPath);
    void onFailure(@NonNull Exception bitmapWorkerException);
}
