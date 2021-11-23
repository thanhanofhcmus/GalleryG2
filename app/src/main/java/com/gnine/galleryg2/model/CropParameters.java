package com.gnine.galleryg2.model;

import android.graphics.Bitmap;

public class CropParameters {
    private final int maxResultImageSizeX;
    private final int maxResultImageSizeY;
    private final Bitmap.CompressFormat compressFormat;
    private final int compressQuality;
    private final String imageInputPath;
    private final String imageOutputPath;
    private final ExifInformation exifInformation;

    public CropParameters(int maxResultImageSizeX, int maxResultImageSizeY,
                          Bitmap.CompressFormat compressFormat, int compressQuality,
                          String imageInputPath, String imageOutputPath, ExifInformation exifInformation) {
        this.maxResultImageSizeX = maxResultImageSizeX;
        this.maxResultImageSizeY = maxResultImageSizeY;
        this.compressFormat = compressFormat;
        this.compressQuality = compressQuality;
        this.imageInputPath = imageInputPath;
        this.imageOutputPath = imageOutputPath;
        this.exifInformation = exifInformation;
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

    public ExifInformation getExifInfor() {
        return exifInformation;
    }
}
