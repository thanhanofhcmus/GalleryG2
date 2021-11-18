package com.gnine.galleryg2.data;

import java.util.ArrayList;
import java.util.List;

public class TypeData {
    public final int resourceId;
    public final String title;
    public final ArrayList<ImageData> list;

    public TypeData(int resourceId, String title, ArrayList<ImageData> list) {
        this.resourceId = resourceId;
        this.title = title;
        this.list = list;
    }
}
