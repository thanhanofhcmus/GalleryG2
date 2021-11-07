package com.gnine.galleryg2.tools;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.gnine.galleryg2.data.ImageData;

import java.util.ArrayList;

public class ImageLoader {

    @NonNull
    public static ArrayList<ImageData> loadImageFromSharedStorage(Context context) {
        ArrayList<ImageData> imageDataList = new ArrayList<>();

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
}
