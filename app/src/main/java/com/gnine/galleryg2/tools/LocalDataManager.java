package com.gnine.galleryg2.tools;

import android.content.Context;

import com.gnine.galleryg2.MySharedPreferences;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.data.TrashData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalDataManager {
    final private static String ALBUM_PREFIX = "G2ALBUMS";
    final private static String FAV_ALBUM_NAME = "Favorites";
    final private static String ALBUM_LIST_KEY = "ALBUMS";

    private static LocalDataManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new LocalDataManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static LocalDataManager getInstance() {
        if (instance == null) {
            instance = new LocalDataManager();
        }
        return instance;
    }

    public static MySharedPreferences getSharedPreferences() {
        return LocalDataManager.getInstance().mySharedPreferences;
    }

    public static void putString(String key, String value) {
        getSharedPreferences().putStringValue(key, value);
    }

    public static String getString(String key) {
        return getSharedPreferences().getStringValue(key);
    }

    //list object
    public static void setObjectListData(String key, ArrayList<TrashData> list) {
        getSharedPreferences().saveObjectList(key, list);
    }

    public static ArrayList<TrashData> getObjectListData(String key) {
        return getSharedPreferences().getObjectList(key);
    }

    //albums
    public static void setAlbumsNames(ArrayList<String> albumsList) {
        putString(ALBUM_LIST_KEY, String.join(" //|//|// ", albumsList));
    }

    public static void importImageToExistingOrNewAlbum(String title, ArrayList<String> imagesPath) {
        Stream<String> currentData = getSingleAlbumData(title).stream()
                .map(imageData -> imageData.uri.getPath());
        List<String> arr = Stream.concat(imagesPath.stream(), currentData)
                .distinct()
                .collect(Collectors.toList());
        putString(ALBUM_PREFIX + title, String.join(" $ ", arr));
    }

    public static void removeImagesFromAlbum(String title, ArrayList<String> imagesPath) {
        ArrayList<String> currentData = getSingleAlbumData(title).stream()
                .map(imageData -> imageData.uri.getPath())
                .collect(Collectors.toCollection(ArrayList::new));
        currentData.removeIf(imagesPath::contains);
        putString(ALBUM_PREFIX + title, String.join(" $ ", currentData));
    }

    public static void removeSingleAlbum(String title) {
        ArrayList<String> allAlbums = getAlbumsNames();
        allAlbums.remove(title);
        setAlbumsNames(allAlbums);
        getSharedPreferences().removeKeyValuePair(ALBUM_PREFIX + title);
    }

    public static ArrayList<String> getAlbumsNames() {
        String albumsNamesString = getString(ALBUM_LIST_KEY);
        if (albumsNamesString != null && !albumsNamesString.equals(""))
            return new ArrayList<>(Arrays.asList(albumsNamesString.split(" //\\|//\\|// ")));
        return new ArrayList<>();
    }

    public static List<FolderData> getAllAlbumsData() {
        return getAlbumsNames().stream()
                .map(title -> new FolderData(title, getSingleAlbumData(title)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ImageData> getSingleAlbumData(String title) {
        final String albumString = getString(ALBUM_PREFIX + title);
        return albumString != null && !albumString.equals("")
                ? ImageLoader.getImageDataFromPath(new ArrayList<>(Arrays.asList(albumString.split(" \\$ "))))
                : new ArrayList<>();
    }

    public static ArrayList<ImageData> getFavAlbum() {
        return getSingleAlbumData(FAV_ALBUM_NAME);
    }

    public static void importImageIntoFav(String imagePath) {
        importImageToExistingOrNewAlbum(FAV_ALBUM_NAME, new ArrayList<>(Collections.singletonList(imagePath)));
    }

    public static void removeImageFromFav(String imagePath) {
        removeImagesFromAlbum(FAV_ALBUM_NAME, new ArrayList<>(Collections.singletonList(imagePath)));
    }
}