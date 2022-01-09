package com.gnine.galleryg2.tools;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.ImageData;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ImageLoader {

    @NonNull
    public static ArrayList<ImageData> getAllImage(Context context) {
        ArrayList<ImageData> imageDataList = new ArrayList<>();

        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
        };
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = context.getApplicationContext().getContentResolver().query(
                contentUri, projection, null, null, sortOrder);

        int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

        while (cursor.moveToNext()) {
            File file = new File(cursor.getString(pathColumn));
            Uri fileUri = Uri.fromFile(file);
            String name = cursor.getString(nameColumn);
            String folder = Objects.requireNonNull(file.getParentFile()).getName();
            int size = cursor.getInt(sizeColumn);
            long dateAdded = cursor.getLong(dateAddedColumn);
            imageDataList.add(new ImageData(fileUri, name, folder, size, dateAdded * 1000L));
        }

        cursor.close();

        return imageDataList;
    }

    public static ArrayList<FolderData> getAllFolder(Context context) {
        ArrayList<ImageData> imageDataList = getAllImage(context);
        ArrayList<FolderData> folderDataList = new ArrayList<>();

        if (imageDataList.size() > 0) {
            imageDataList.sort((image1, image2) -> image2.folder.compareTo(image1.folder));

            String folderCur = imageDataList.get(0).folder;

            ArrayList<ImageData> images = new ArrayList<>();
            images.add(imageDataList.get(0));

            for (int i = 1; i < imageDataList.size(); i++) {
                if (!folderCur.equals(imageDataList.get(i).folder)) {
                    folderDataList.add(new FolderData(folderCur, images));
                    folderCur = imageDataList.get(i).folder;
                    images = new ArrayList<>();
                }
                images.add(imageDataList.get(i));
            }
            folderDataList.add(new FolderData(folderCur, images));
        }
        return folderDataList;
    }

    public static ArrayList<ImageData> getImagesFromFolder(Context context, String folderName) {
        ArrayList<ImageData> imageDataList = getAllImage(context);
        ArrayList<ImageData> images = new ArrayList<>();
        for (int i = 0; i <imageDataList.size(); i++) {
            if (folderName.equals(imageDataList.get(i).folder)) {
                images.add(imageDataList.get(i));
            }
        }
        return images;
    }

    public static ArrayList<ImageData> getImageDataFromPath(ArrayList<String> pathList) {
        ArrayList<ImageData> res = new ArrayList<>();
        for (String itemPath : pathList) {
            File temp = new File(itemPath);
            if (temp.exists()) {
                FileTime ft = null;
                try {
                    ft = (FileTime) Files.getAttribute(temp.toPath(), "creationTime");
                } catch (IOException ignored) {
                }
                res.add(new ImageData(Uri.fromFile(temp), temp.getName(), temp.getParentFile().getName(), (int)temp.length(), ft != null ? ft.toMillis() : 0));
            }
        }
        res.sort((o1, o2) -> o1.dateString.compareTo(o2.dateString) * (-1));
        return res;
    }
}
