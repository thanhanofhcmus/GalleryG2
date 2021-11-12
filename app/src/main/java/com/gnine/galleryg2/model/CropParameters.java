package com.gnine.galleryg2.model;

import android.graphics.Bitmap;

public class CropParameters {
    private int maxResultImageSizeX,maxResultImageSizeY;
    private Bitmap.CompressFormat compressFormat;
    private int compressQuality;
    private String imageInputPath,imageOutputPath;
    private ExifInfor exifInfor;

    public CropParameters(int maxResultImageSizeX,int maxResultImageSizeY,
                          Bitmap.CompressFormat compressFormat, int compressQuality,
                          String imageInputPath,String imageOutputPath,ExifInfor exifInfor){
        this.maxResultImageSizeX=maxResultImageSizeX;
        this.maxResultImageSizeY=maxResultImageSizeY;
        this.compressFormat=compressFormat;
        this.compressQuality=compressQuality;
        this.imageInputPath=imageInputPath;
        this.imageOutputPath=imageOutputPath;
        this.exifInfor=exifInfor;
    }

    public int getMaxResultImageSizeX() {
        return maxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return maxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public int getCompressQuality() {
        return compressQuality;
    }

    public String getImageInputPath() {
        return imageInputPath;
    }

    public String getImageOutputPath() {
        return imageOutputPath;
    }

    public ExifInfor getExifInfor() {
        return exifInfor;
    }
}
