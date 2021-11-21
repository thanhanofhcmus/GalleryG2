package com.gnine.galleryg2;

import android.content.Context;
import android.content.SharedPreferences;

import com.gnine.galleryg2.data.TrashData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private final Context mContext;

    public MySharedPreferences(Context mContext){
        this.mContext = mContext;
    }

    public void  putIntValue(String key,int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key,value);
        editor.apply();

    }

    public int getIntValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }
    public void  putStringValue(String key,String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key,value);
        editor.apply();

    }

    public String getStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    public void saveObjectList(String key, ArrayList<TrashData> list){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gs = new Gson();
        String json = gs.toJson(list);
        editor.putString(key,json);
        editor.apply();
    }

    public ArrayList<TrashData> getObjectList(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);

        Gson gs = new Gson();
        String json = sharedPreferences.getString(key,null);
        Type type = new TypeToken<ArrayList<TrashData>>() {}.getType();

        ArrayList<TrashData> ans = gs.fromJson(json,type);
        if (ans == null) {
            return new ArrayList<>();
        }
        return ans;
    }
}