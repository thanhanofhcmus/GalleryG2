package com.gnine.galleryg2.data;

public class RecyclerData {
    public enum Type {
        Label,
        Image
    }
    public final Type type;
    public final String labelData;
    public final ImageData imageData;
    public final int index;

    public RecyclerData(Type type, String labelData, ImageData imageData, int index) {
        this.type = type;
        this.labelData = labelData;
        this.imageData = imageData;
        this.index = index;
    }
}
