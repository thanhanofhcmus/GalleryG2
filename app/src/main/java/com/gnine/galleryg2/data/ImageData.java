package com.gnine.galleryg2.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ImageData implements Parcelable {

    public final Uri uri;
    public final String name;
    public final int size;
    public final long dateAdded;
    public final String dateString;
    private boolean checked;

    public ImageData(Uri uri, String name, int size, long dateAdded) {
        this.uri = uri;
        this.name = name;
        this.size = size;
        this.dateAdded = dateAdded;
        this.dateString = new SimpleDateFormat("yyyyMMdd:hh:mm", Locale.getDefault()).format(this.dateAdded);
    }

    protected ImageData(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.name = in.readString();
        this.size = in.readInt();
        this.dateAdded = in.readLong();
        this.checked = in.readByte() != 0;
        this.dateString = new SimpleDateFormat("yyyyMMdd:hh:mm", Locale.getDefault()).format(this.dateAdded);
    }

    public String getDateTime() {
        return dateString;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(uri, i);
        parcel.writeString(name);
        parcel.writeInt(size);
        parcel.writeLong(dateAdded);
        parcel.writeByte((byte) (checked ? 1 : 0));
    }
}
