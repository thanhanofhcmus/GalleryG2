package com.gnine.galleryg2.fragments;

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
import com.gnine.galleryg2.tools.ErrorDialog;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.google.android.material.snackbar.Snackbar;

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
    final private View parentView;

    public AlbumDialog(View parentView) {
        this.parentView = parentView;
    }

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
        final Context context = requireContext();

        createBtn.setOnClickListener(view1 -> {
            if (!input.getText().toString().equals("")) {
                String newAlbum = input.getText().toString();
                if (LocalDataManager.getAlbumsNames().contains(newAlbum) || newAlbum.equalsIgnoreCase("FAVORITES")) {
                    ErrorDialog.show(context, "This album already exists");
                } else {
                    ArrayList<String> currentData = LocalDataManager.getAlbumsNames();
                    System.out.println(currentData);
                    currentData.add(newAlbum);
                    LocalDataManager.setAlbumsNames(currentData);
                    System.out.println(LocalDataManager.getAlbumsNames());
                    Snackbar.make(parentView, "Album created", Snackbar.LENGTH_SHORT).show();

                    Objects.requireNonNull(getDialog()).dismiss();
                }
            }
            else {
                ErrorDialog.show(context, "Please type the name of the new album");
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
