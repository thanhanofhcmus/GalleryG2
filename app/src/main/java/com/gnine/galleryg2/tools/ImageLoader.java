package com.gnine.galleryg2.tools;

import static java.security.AccessController.getContext;

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
import java.util.List;
import java.util.Objects;

public class ImageLoader {

    @NonNull
    public static ArrayList<ImageData> loadImageFromSharedStorage(Context context) {
        ArrayList<ImageData> imageDataList = new ArrayList<>();

        Uri collection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
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
        return filePath.endsWith(".jpg") || filePath.endsWith(".png") || filePath.endsWith(".jpeg")
                || filePath.endsWith(".gif") || filePath.endsWith(".webp") || filePath.endsWith(
                ".bmp");
    }

    private static boolean isVideoFile(String filePath) {
        return filePath.endsWith(".mp4") || filePath.endsWith(".mkv") || filePath.endsWith(".3gp")
                || filePath.endsWith(".webm");
    }

    private static class ImageAndVideoFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return isImageFile(file.getAbsolutePath()) || isVideoFile(file.getAbsolutePath());
        }
    }

    private static class SubDirFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isDirectory();
        }
    }

    private static class MyFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isDirectory() || isImageFile(file.getAbsolutePath()) || isVideoFile(
                    file.getAbsolutePath());
        }
    }

    private static ArrayList<ImageData> getImagesFromFolder(String directoryPath) {
        ArrayList<ImageData> imagesList = new ArrayList<>();
        File[] files = new File(directoryPath).listFiles(new ImageAndVideoFileFilter());
        assert files != null;
        for (File file : files) {
            if (file == null) {
                continue;
            }
            FileTime fileTime = null;
            try {
                fileTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
            } catch (IOException ignored) {
            }
            imagesList.add(new ImageData(Uri.fromFile(file), file.getName(), (int) file.length(),
                    fileTime != null ? fileTime.toMillis() : 0));
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
                    if (file.listFiles(new ImageAndVideoFileFilter()) != null) { //contains images
                        if (file.listFiles(new ImageAndVideoFileFilter()).length > 0) {
                            foldersList.add(new FolderData(R.drawable.ic_folder,
                                    Uri.fromFile(file),
                                    file.getPath(),
                                    getImagesFromFolder(file.getPath())));
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

    public static ArrayList<ImageData> getAllImagesFromDevice(Context context) {
        ArrayList<ImageData> res = new ArrayList<>();
        List<FolderData> list = new ArrayList<>();
        if (ImageLoader.retrieveFoldersHaveImage("/storage/") != null)
            list.addAll(Objects.requireNonNull(ImageLoader.retrieveFoldersHaveImage("/storage/")));
        if (ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath()) != null)
            list.addAll(Objects.requireNonNull(ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath())));
        for (FolderData folder : list)
            res.addAll(getImagesFromFolder(folder.uri.getPath()));
        return res;
    }
}
