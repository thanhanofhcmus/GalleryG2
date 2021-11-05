package com.gnine.galleryg2;

import java.net.URI;
import java.util.List;

public class Folder {
    private int resourceId;
    private URI uri;
    private String title;
    private List<ImageData> imageList;

    public Folder(int resourceId, URI uri, String title, List<ImageData> list) {
        this.resourceId = resourceId;
        this.uri = uri;
        this.title = title;
        this.imageList = list;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageData> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageData> imageList) {
        this.imageList = imageList;
    }
}
