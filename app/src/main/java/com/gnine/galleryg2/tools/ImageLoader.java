package com.gnine.galleryg2.tools;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageLoader {
    private static final int TEST_IMAGES_NUMBER = 23;

    @NonNull
    public static int[] loadTestImageIdNTimes(int n) {
        int[] resourceId = new int[TEST_IMAGES_NUMBER * n];
        for (int j = 0; j < n; ++j) {
            for (int i = 0; i < TEST_IMAGES_NUMBER; ++i) {
                resourceId[j * TEST_IMAGES_NUMBER + i] = getResourceId("image_" + i, R.drawable.class);
            }
        }
        return resourceId;
    }

    @NonNull
    public static List<ImageData> loadUriImageFromSharedStorage(Context context) {
        Uri collection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                       ? MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                       : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] { MediaStore.Images.Media._ID };
        List<Uri> imageUris = new ArrayList<>();

        Cursor cursor = context.getApplicationContext().getContentResolver()
                .query(collection, projection, null, null, null);

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            imageUris.add(contentUri);
        }

        cursor.close();
        return imageUris.stream()
                .map(uri -> new ImageData(uri, null, ImageData.UNDEFINED_INT_DATA, ImageData.UNDEFINED_INT_DATA))
                .collect(Collectors.toList());
    }

    @NonNull
    public static List<ImageData> loadImageFromSharedStorage(Context context) {
        List<ImageData> imageDataList = new ArrayList<>();

        Uri collection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                       ? MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                       : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
        };
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = context.getApplicationContext().getContentResolver().query(
                collection, projection, null, null, sortOrder);

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(idColumn);
            String name = cursor.getString(nameColumn);
            int size = cursor.getInt(sizeColumn);
            long dateAdded = cursor.getLong(dateAddedColumn);
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            imageDataList.add(new ImageData(contentUri, name, size, dateAdded));
        }

        cursor.close();

        return imageDataList;
    }

    private static int getResourceId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
