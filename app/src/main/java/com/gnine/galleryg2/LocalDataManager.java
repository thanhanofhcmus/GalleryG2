package com.gnine.galleryg2;

import android.content.Context;

import com.gnine.galleryg2.data.TrashData;

import java.util.ArrayList;

public class LocalDataManager {
    private static final String PREF_FIRST_INSTALL = "PREF_FIRST_INSTALL";
    private static LocalDataManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new LocalDataManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public  static LocalDataManager getInstance(){
        if(instance == null){
            instance = new LocalDataManager();
        }
        return instance;
    }
    //layout
    public static void setLayoutSetting(int isFirst){
        LocalDataManager.getInstance().mySharedPreferences.putIntValue("LAYOUT",isFirst);
    }

    public static int getLayoutSetting(){
        return LocalDataManager.getInstance().mySharedPreferences.getIntValue("LAYOUT");
    }
    //list object
    public static void setObjectListData(String key, ArrayList<TrashData> list){
        LocalDataManager.getInstance().mySharedPreferences.saveObjectList(key,list);
    }
    public static ArrayList<TrashData> getObjectListData(String key){
        return LocalDataManager.getInstance().mySharedPreferences.getObjectList(key);
    }

    //setting
    public static void setSetting(String isFirst){
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("SETTING",isFirst);
    }

    public static String getSetting(){
        return LocalDataManager.getInstance().mySharedPreferences.getStringValue("SETTING");
    }
}