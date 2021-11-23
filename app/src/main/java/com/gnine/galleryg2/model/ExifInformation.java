package com.gnine.galleryg2.model;

public class ExifInformation {

    private final int exifOrientation;
    private final int exifDegrees;
    private final int exifTranslation;

    public ExifInformation(int exifOrientation, int exifDegrees, int exifTranslation) {
        this.exifOrientation = exifOrientation;
        this.exifDegrees = exifDegrees;
        this.exifTranslation = exifTranslation;
    }

    public int getExifDegrees() {
        return exifDegrees;
    }

    public int getExifTranslation() {
        return exifTranslation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExifInformation exifInformation = (ExifInformation) o;
        return exifOrientation == exifInformation.exifOrientation && exifDegrees == exifInformation.exifDegrees && exifTranslation == exifInformation.exifTranslation;
    }

    @Override
    public int hashCode() {
        int result = exifOrientation;
        result = 31 * result + exifDegrees;
        result = 31 * result + exifTranslation;
        return result;
    }
}
