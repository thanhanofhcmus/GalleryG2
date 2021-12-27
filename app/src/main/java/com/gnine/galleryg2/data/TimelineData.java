package com.gnine.galleryg2.data;

public class TimelineData {
    public enum Type {
        Time,
        Image
    }
    public final Type type;
    public final String time;
    public final ImageData imageData;
    public final int index;

    public TimelineData(Type type, String time, ImageData imageData, int index) {
        this.type = type;
        this.time = time;
        this.imageData = imageData;
        this.index = index;
    }
}
