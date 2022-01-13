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

            imageDataList.add(new ImageData(contentUri, name, size, dateAdded * 1000L));
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

    public static ArrayList<ImageData> getImagesFromFolder(String directoryPath) {
        ArrayList<ImageData> imagesList = new ArrayList<>();
        File[] files = new File(directoryPath).listFiles(new ImageAndVideoFileFilter());
        if (files != null) {
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
        }
        imagesList.sort((o1, o2) -> {
            if (o1.getDateTime() == null || o2.getDateTime() == null)
                return 0;
            return o1.getDateTime().compareTo(o2.getDateTime());
        });
        return imagesList;
    }

    public static List<FolderData> retrieveFoldersHaveImage(String directoryPath) {
        List<FolderData> foldersList = new ArrayList<>();
        File folderName = new File(directoryPath);
        if (!folderName.getName().equals(".nomedia") && !folderName.getName().equals(".thumbnails")) {
            File[] files = new File(directoryPath).listFiles(new MyFilter());
            if (files != null) {
                for (File file : files) {
                    // Add the directories containing images or sub-directories
                    if (file.isDirectory() && !file.getName().equals(".nomedia") && !file.getName().equals(".thumbnails")) {
                        if (file.listFiles(new ImageAndVideoFileFilter()) != null) { //contains images
                            if (Objects.requireNonNull(file.listFiles(new ImageAndVideoFileFilter())).length > 0) {
                                foldersList.add(new FolderData(R.drawable.ic_folder,
                                        Uri.fromFile(file),
                                        file.getPath(),
                                        getImagesFromFolder(file.getPath())));
                            }
                        }
                        if (file.listFiles(new SubDirFilter()) != null) {
                            foldersList.addAll(Objects.requireNonNull(retrieveFoldersHaveImage(file.getPath())));
                        }
                    }
                }
            }
        }
        return foldersList;
    }

    public static ArrayList<ImageData> getAllImagesFromDevice() {
        ArrayList<ImageData> res = new ArrayList<>();
        List<FolderData> list = new ArrayList<>();
        list.addAll(ImageLoader.retrieveFoldersHaveImage("/storage/"));
        list.addAll(ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath()));
        for (FolderData folder : list)
            res.addAll(getImagesFromFolder(folder.uri.getPath()));
        res.sort((o1, o2) -> o1.dateString.compareTo(o2.dateString) * (-1));
        return res;
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
                res.add(new ImageData(Uri.fromFile(temp), temp.getName(), (int)temp.length(), ft != null ? ft.toMillis() : 0));
            }
        }
        res.sort((o1, o2) -> o1.dateString.compareTo(o2.dateString) * (-1));
        return res;
    }
}