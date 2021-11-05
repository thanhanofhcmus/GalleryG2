package com.gnine.galleryg2.data;

import java.util.List;

public class TypeData {
    public final int resourceId;
    public final String title;
    public final List<ImageData> list;

    public TypeData(int resourceId, String title, List<ImageData> list) {
        this.resourceId = resourceId;
        this.title = title;
        this.list = list;
    }
}
