package com.gnine.galleryg2;

import com.gnine.galleryg2.data.ImageData;

import java.util.ArrayList;
import java.util.List;

public interface ISendImageListListener {
    void sendImageList(ArrayList<ImageData> list);
    void sendImagePosition(int position);
}
