package com.gnine.galleryg2.data;

import android.net.Uri;

import java.util.ArrayList;

public class FolderData {
    public final String title;
    public final ArrayList<ImageData> imageList;

    public FolderData(String title, ArrayList<ImageData> list) {
        this.title = title;
        this.imageList = list;
    }
}
