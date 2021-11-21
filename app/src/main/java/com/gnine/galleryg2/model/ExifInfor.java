package com.gnine.galleryg2.model;

public class ExifInfor {

    private int exifOrientation;
    private int exifDegrees;
    private int exifTranslation;

    public ExifInfor(int exifOrientation,int exifDegrees,int exifTranslation){
        this.exifOrientation=exifOrientation;
        this.exifDegrees=exifDegrees;
        this.exifTranslation=exifTranslation;
    }

    public int getExifDegrees(){ return exifDegrees; }
    public int getExifTranslation() {return exifTranslation;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExifInfor exifInfor = (ExifInfor) o;
        return exifOrientation == exifInfor.exifOrientation && exifDegrees == exifInfor.exifDegrees && exifTranslation == exifInfor.exifTranslation;
    }

    @Override
    public int hashCode() {
        int result =exifOrientation;
        result= 31*result +exifDegrees;
        result=31*result+exifTranslation;
        return result;
    }
}
