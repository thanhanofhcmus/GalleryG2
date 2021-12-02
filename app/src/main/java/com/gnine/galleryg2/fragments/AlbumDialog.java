package com.gnine.galleryg2.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.tools.LocalDataManager;

import java.util.ArrayList;
import java.util.Objects;

public class AlbumDialog extends DialogFragment {
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    private EditText input;

    public AlbumDialog() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = view.findViewById(R.id.albumId);
        Button createBtn = view.findViewById(R.id.confirmBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        createBtn.setOnClickListener(view1 -> {
            if (!input.getText().toString().equals("")) {
                String newAlbum = input.getText().toString();
                if (LocalDataManager.getAlbumsNames().contains(newAlbum) || newAlbum.equalsIgnoreCase("FAVORITES")) {
                    AlertDialog ad = new AlertDialog.Builder(getContext())
                            .setTitle("This album already exists!!!")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .create();
                    ad.show();
                } else {
                    ArrayList<String> currentData = LocalDataManager.getAlbumsNames();
                    System.out.println(currentData);
                    currentData.add(newAlbum);
                    LocalDataManager.setAlbumsNames(currentData);
                    System.out.println(LocalDataManager.getAlbumsNames());
                    AlertDialog ad = new AlertDialog.Builder(getContext())
                            .setTitle("Create new album successfully!!!")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .create();
                    ad.show();

                    Objects.requireNonNull(getDialog()).dismiss();
                }
            }
            else {
                AlertDialog ad = new AlertDialog.Builder(getContext())
                        .setTitle("Please type the name of the new album!!!")
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .create();
                ad.show();
            }
        });
        cancelBtn.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
