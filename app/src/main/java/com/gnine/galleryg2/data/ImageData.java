package com.gnine.galleryg2.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageData implements Parcelable {

    public final Uri uri;
    public final String name;
    public final int size;
    public final long dateAdded;
    private boolean checked;

    public ImageData(Uri uri, String name, int size, long dateAdded) {
        this.uri = uri;
        this.name = name;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    protected ImageData(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        name = in.readString();
        size = in.readInt();
        dateAdded = in.readLong();
        checked = in.readByte() != 0;
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
