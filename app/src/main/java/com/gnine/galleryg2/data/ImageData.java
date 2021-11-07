package com.gnine.galleryg2.data;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageData {
    public final Uri uri;
    public final String name;
    public final int size;
    public final long dateAdded;
    public boolean checked;

    public ImageData(Uri uri, String name, int size, long dateAdded) {
        this.uri = uri;
        this.name = name;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public String getDateTime() {
       return new SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault())
               .format(new Date(dateAdded * 1000L));
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
