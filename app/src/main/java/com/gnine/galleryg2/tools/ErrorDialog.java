package com.gnine.galleryg2.tools;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ErrorDialog {
    public static void show(Context context, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("GOT IT", null)
                .show();
    }
}
