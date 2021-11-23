package com.gnine.galleryg2.model;

import android.graphics.RectF;

public class ImageState {
    private final RectF cropRect;
    private final RectF currentImageRect;

    private final float currentScale;
    private final float currentAngle;

    public ImageState(RectF cropRect, RectF currentImageRect,
                      float currentScale, float currentAngle) {
        this.cropRect = cropRect;
        this.currentImageRect = currentImageRect;
        this.currentScale = currentScale;
        this.currentAngle = currentAngle;
    }

    public RectF getCropRect() {
        return cropRect;
    }

    public RectF getCurrentImageRect() {
        return currentImageRect;
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }
}
