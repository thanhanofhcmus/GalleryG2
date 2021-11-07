package com.gnine.galleryg2.data;

import android.net.Uri;

import java.util.ArrayList;

public class FolderData {
    public final int resourceId;
    public final Uri uri;
    public final String title;
    public final ArrayList<ImageData> imageList;

    public FolderData(int resourceId, Uri uri, String title, ArrayList<ImageData> list) {
        this.resourceId = resourceId;
        this.uri = uri;
        this.title = title;
        this.imageList = list;
    }
}
