package com.gnine.galleryg2.tools;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.R;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImageLoader {
    //private static final int TEST_IMAGES_NUMBER = 23;

    /*@NonNull
    public static int[] loadTestImageIdNTimes(int n) {
        int[] resourceId = new int[TEST_IMAGES_NUMBER * n];
        for (int j = 0; j < n; ++j) {
            for (int i = 0; i < TEST_IMAGES_NUMBER; ++i) {
                resourceId[j * TEST_IMAGES_NUMBER + i] = getResourceId("image_" + i, R.drawable.class);
            }
        }
        return resourceId;
    }*/

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

    private static boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".jpeg") || filePath.endsWith(".gif") || filePath.endsWith(".webp") || filePath.endsWith(".bmp"))
            return true;
        return false;
    }

    private static boolean isVideoFile(String filePath) {
        if (filePath.endsWith(".mp4") || filePath.endsWith(".mkv") || filePath.endsWith(".3gp") || filePath.endsWith(".webm"))
            return true;
        return false;
    }

    private static class ImageAndVideoFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (isImageFile(file.getAbsolutePath()) || isVideoFile(file.getAbsolutePath()))
                return true;
            return false;
        }
    }

    private static class SubDirFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory())
                return true;
            return false;
        }
    }

    private static class MyFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory())
                return true;
            if (isImageFile(file.getAbsolutePath()) || isVideoFile(file.getAbsolutePath()))
                return true;
            return false;
        }
    }

    private static List<ImageData> getImagesFromFolder(String directoryPath) {
        List<ImageData> imagesList = new ArrayList<>();
        File[] files = new File(directoryPath).listFiles(new ImageAndVideoFileFilter());
        assert files != null;
        for (File file : files) {
            FileTime temp = null;
            try {
                temp = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            } catch (IOException e) {
                
            }
            imagesList.add(new ImageData(Uri.fromFile(file), file.getName(), (int)file.length(), temp != null ? temp.toMillis() : 0 ));
        }
        return imagesList;
    }

    public static List<FolderData> retrieveFoldersHaveImage(String directoryPath) {
        List<FolderData> foldersList = new ArrayList<>();
        File[] files = new File(directoryPath).listFiles(new MyFilter());
        if (files != null) {
            for (File file : files) {
                // Add the directories containing images or sub-directories
                if (file.isDirectory()) {
                    if (file.listFiles(new ImageAndVideoFileFilter()) != null) {//contains images
                        if (file.listFiles(new ImageAndVideoFileFilter()).length > 0) {
                            FolderData tempFolder = new FolderData(R.drawable.ic_folder, Uri.parse(file.getPath()), file.getPath(), getImagesFromFolder(file.getPath()));
                            foldersList.add(tempFolder);
                        }
                    }
                    if (file.listFiles(new SubDirFilter()) != null) {
                        foldersList.addAll(retrieveFoldersHaveImage(file.getPath()));
                    }
                }
            }
            return foldersList;
        }
        return null;
    }

    public static List<ImageData> filterImageFromDataByFolderURI(List<ImageData> imagesList, Uri uri) {
        List<ImageData> filteredList = new ArrayList<>();
        for (int i = 0; i < imagesList.size(); i++) {
            String tempStr = imagesList.get(i).uri.getPath();
            if (uri.getPath().equals(tempStr.substring(0, tempStr.lastIndexOf("/"))))//.substring(0, tempStr.lastIndexOf("/"))))
                filteredList.add(imagesList.get(i));
        }
        return filteredList;
    }

    /*private static int getResourceId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }*/
}
