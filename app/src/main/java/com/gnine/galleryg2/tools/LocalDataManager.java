package com.gnine.galleryg2.tools;

import android.content.Context;

import com.gnine.galleryg2.MySharedPreferences;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.data.TrashData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LocalDataManager {
    private static final String PREF_FIRST_INSTALL = "PREF_FIRST_INSTALL";
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

    //layout
    public static void setLayoutSetting(int isFirst) {
        LocalDataManager.getInstance().mySharedPreferences.putIntValue("LAYOUT", isFirst);
    }

    public static int getLayoutSetting() {
        return LocalDataManager.getInstance().mySharedPreferences.getIntValue("LAYOUT");
    }

    //list object
    public static void setObjectListData(String key, ArrayList<TrashData> list) {
        LocalDataManager.getInstance().mySharedPreferences.saveObjectList(key, list);
    }

    public static ArrayList<TrashData> getObjectListData(String key) {
        return LocalDataManager.getInstance().mySharedPreferences.getObjectList(key);
    }

    //setting
    public static void setSetting(String isFirst) {
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("SETTING", isFirst);
    }

    public static String getSetting() {
        return LocalDataManager.getInstance().mySharedPreferences.getStringValue("SETTING");
    }

    //albums
    public static void setAlbumsNames(ArrayList<String> albumsList) {
        StringBuilder saveRes = new StringBuilder();
        for (int i = 0; i < albumsList.size(); i++) {
            if (i != albumsList.size() - 1)
                saveRes.append(albumsList.get(i)).append(" //|//|// ");
            else
                saveRes.append(albumsList.get(i));
        }
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("ALBUMS",saveRes.toString());
    }

    public static void importImageToExistingOrNewAlbum(String title, ArrayList<String> imagesPath) {
        ArrayList<String> currentData = getSingleAlbumData(title).stream()
                .map(imageData -> imageData.uri.getPath())
                .collect(Collectors.toCollection(ArrayList::new));
        StringBuilder saveData = new StringBuilder();
        if (currentData.size() == 0) {
            for (int i = 0; i < imagesPath.size(); i++) {
                if (i != imagesPath.size() - 1)
                    saveData.append(imagesPath.get(i)).append(" $ ");
                else
                    saveData.append(imagesPath.get(i));
            }
        } else {
            for (String item : imagesPath) {
                if (!currentData.contains(item)) {
                    currentData.add(item);
                }
            }
            for (int i = 0; i < currentData.size(); i++) {
                if (i != currentData.size() - 1)
                    saveData.append(currentData.get(i)).append(" $ ");
                else
                    saveData.append(currentData.get(i));
            }
        }
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("G2ALBUMS"+title,saveData.toString());
    }

    public static void removeImagesFromAlbum(String title, ArrayList<String> imagesPath) {
        ArrayList<String> currentData = getSingleAlbumData(title).stream()
                .map(imageData -> imageData.uri.getPath())
                .collect(Collectors.toCollection(ArrayList::new));
        currentData.removeIf(imagesPath::contains);
        StringBuilder saveData = new StringBuilder();
        for (int i = 0; i < currentData.size(); i++) {
            if (i != currentData.size() - 1)
                saveData.append(currentData.get(i)).append(" $ ");
            else
                saveData.append(currentData.get(i));
        }
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("G2ALBUMS"+title,saveData.toString());
    }

    public static void removeSingleAlbum(String title) {
        ArrayList<String> allAlbums = getAlbumsNames();
        allAlbums.remove(title);
        setAlbumsNames(allAlbums);
        LocalDataManager.getInstance().mySharedPreferences.removeKeyValuePair("G2ALBUMS" + title);
    }

    public static ArrayList<String> getAlbumsNames() {
        String albumsNamesString = LocalDataManager.getInstance().mySharedPreferences.getStringValue("ALBUMS");
        if (albumsNamesString != null && !albumsNamesString.equals(""))
            return new ArrayList<>(Arrays.asList(albumsNamesString.split(" //\\|//\\|// ")));
        return new ArrayList<>();
    }

    public static List<FolderData> getAllAlbumsData() {
        List<FolderData> myAlbums = new ArrayList<>();
        ArrayList<String> albumsNamesKey = getAlbumsNames();
        for (String title : albumsNamesKey)
            myAlbums.add(new FolderData(R.drawable.ic_folder, null, title, getSingleAlbumData(title)));
        return myAlbums;
    }

    public static ArrayList<ImageData> getSingleAlbumData(String title) {
        if (LocalDataManager.getInstance().mySharedPreferences.getStringValue("G2ALBUMS" + title) != null && !LocalDataManager.getInstance().mySharedPreferences.getStringValue("G2ALBUMS" + title).equals("")) {
            ArrayList<ImageData> res = ImageLoader.getImageDataFromPath(new ArrayList<>(Arrays.asList(LocalDataManager.getInstance().mySharedPreferences.getStringValue("G2ALBUMS" + title).split(" \\$ "))));
            ArrayList<String> checkedExistList = res.stream()
                    .map(imageData -> imageData.uri.getPath())
                    .collect(Collectors.toCollection(ArrayList::new));
            StringBuilder saveData = new StringBuilder();
            for (int i = 0; i < checkedExistList.size(); i++) {
                if (i != checkedExistList.size() - 1)
                    saveData.append(checkedExistList.get(i)).append(" $ ");
                else
                    saveData.append(checkedExistList.get(i));
            }
            LocalDataManager.getInstance().mySharedPreferences.putStringValue("G2ALBUMS"+title,saveData.toString());
            return res;
        }
        return new ArrayList<>();
    }

    public static ArrayList<ImageData> getFavAlbum() {
        //G2ALBUMSFAVORITES
        if (LocalDataManager.getInstance().mySharedPreferences.getStringValue("G2ALBUMSFavorites") != null && !LocalDataManager.getInstance().mySharedPreferences.getStringValue("G2ALBUMSFavorites").equals("")) {
            ArrayList<ImageData> res = ImageLoader.getImageDataFromPath(new ArrayList<>(Arrays.asList(LocalDataManager.getInstance().mySharedPreferences.getStringValue("G2ALBUMSFavorites").split(" \\$ "))));
            ArrayList<String> checkedExistList = res.stream()
                    .map(imageData -> imageData.uri.getPath())
                    .collect(Collectors.toCollection(ArrayList::new));
            StringBuilder saveData = new StringBuilder();
            for (int i = 0; i < checkedExistList.size(); i++) {
                if (i != checkedExistList.size() - 1)
                    saveData.append(checkedExistList.get(i)).append(" $ ");
                else
                    saveData.append(checkedExistList.get(i));
            }
            LocalDataManager.getInstance().mySharedPreferences.putStringValue("G2ALBUMSFavorites",saveData.toString());
            return res;
        }
        return new ArrayList<>();
    }

    public static void importImageIntoFav(String imagePath) {
        ArrayList<String> importList = new ArrayList<>();
        importList.add(imagePath);
        importImageToExistingOrNewAlbum("Favorites", importList);
    }

    public static void removeImageFromFav(String imagePath) {
        ArrayList<String> currentData = getFavAlbum().stream()
                .map(imageData -> imageData.uri.getPath())
                .collect(Collectors.toCollection(ArrayList::new));
        currentData.remove(imagePath);
        StringBuilder saveData = new StringBuilder();
        for (int i = 0; i < currentData.size(); i++) {
            if (i != currentData.size() - 1)
                saveData.append(currentData.get(i)).append(" $ ");
            else
                saveData.append(currentData.get(i));
        }
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("G2ALBUMSFavorites", saveData.toString());
    }
}