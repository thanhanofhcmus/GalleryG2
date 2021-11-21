package com.gnine.galleryg2.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class AspectRatio implements Parcelable {

    @Nullable
    private final String aspectRadioTitle;
    private final float aspectRatioX;
    private final float aspectRatioY;

    public AspectRatio(@Nullable String aspectRadioTitle, float aspectRatioX, float aspectRatioY) {
        this.aspectRadioTitle = aspectRadioTitle;
        this.aspectRatioX = aspectRatioX;
        this.aspectRatioY = aspectRatioY;
    }

    protected AspectRatio(Parcel in) {
        aspectRadioTitle = in.readString();
        aspectRatioX = in.readFloat();
        aspectRatioY = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(aspectRadioTitle);
        parcel.writeFloat(aspectRatioX);
        parcel.writeFloat(aspectRatioY);
    }

    public static final Creator<AspectRatio> CREATOR = new Creator<AspectRatio>() {
        @Override
        public AspectRatio createFromParcel(Parcel parcel) {
            return new AspectRatio(parcel);
        }

        @Override
        public AspectRatio[] newArray(int size) {
            return new AspectRatio[size];
        }
    };

    @Nullable
    public String getAspectRatioTitle() {
        return aspectRadioTitle;
    }

    public float getAspectRatioX() {
        return aspectRatioX;
    }

    public float getAspectRatioY() {
        return aspectRatioY;
    }

}
