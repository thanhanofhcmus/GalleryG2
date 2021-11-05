package com.gnine.galleryg2;

import java.util.List;

public class Types {
    private int resrcId;
    private String title;
    private List<ImageData> list;

    public int getResrcId() {
        return resrcId;
    }

    public void setResrcId(int resrcId) {
        this.resrcId = resrcId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageData> getList() {
        return list;
    }

    public void setList(List<ImageData> list) {
        this.list = list;
    }

    public Types(int resrcId, String title, List<ImageData> list) {
        this.resrcId = resrcId;
        this.title = title;
        this.list = list;
    }
}
