package com.gnine.galleryg2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlbumDialog extends Dialog implements View.OnClickListener {
    public Activity a;
    //public Dialog d;
    public Button confirmBtn, cancelBtn;

    public AlbumDialog(Activity a) {
        super(a);
        this.a = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_album);
        confirmBtn = findViewById(R.id.confirmBtn);
        cancelBtn  = findViewById(R.id.cancelBtn);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmBtn:
                dismiss();
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
