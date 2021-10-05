package com.gnine.galleryg2.tools;

import androidx.annotation.NonNull;

import com.gnine.galleryg2.R;

import java.lang.reflect.Field;

public class ImageLoader {
    static private final int TEST_IMAGES_NUMBER = 23;

    @NonNull
    public static int[] loadTestImageId() {
        int[] resourceId = new int[TEST_IMAGES_NUMBER];
        for (int i = 0; i < TEST_IMAGES_NUMBER; ++i) {
            resourceId[i] = getResourceId("image_" + i, R.drawable.class);
        }
        return resourceId;
    }

    @NonNull
    public static int[] loadTestImageIdNTimes(int n) {
        int[] resourceId = new int[TEST_IMAGES_NUMBER * n];
        for (int j = 0; j < n; ++j) {
            for (int i = 0; i < TEST_IMAGES_NUMBER; ++i) {
                resourceId[j * TEST_IMAGES_NUMBER + i] = getResourceId("image_" + i, R.drawable.class);
            }
        }
        return resourceId;
    }

    private static int getResourceId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
