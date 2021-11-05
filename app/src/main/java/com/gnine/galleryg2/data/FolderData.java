package com.gnine.galleryg2.data;

import java.net.URI;
import java.util.List;

public class FolderData {
    public final int resourceId;
    public final URI uri;
    public final String title;
    public final List<ImageData> imageList;

    public FolderData(int resourceId, URI uri, String title, List<ImageData> list) {
        this.resourceId = resourceId;
        this.uri = uri;
        this.title = title;
        this.imageList = list;
    }
}
