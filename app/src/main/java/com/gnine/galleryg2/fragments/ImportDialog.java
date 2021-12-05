package com.gnine.galleryg2.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class ImportDialog extends DialogFragment {
    private EditText input;
    private Spinner option, existingAlbums;
    private ArrayList<String> imagesPath;

    public ImportDialog() {}

    public void setData(ArrayList<String> list) {
        this.imagesPath = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.import_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = view.findViewById(R.id.albumId);
        option = view.findViewById(R.id.optionSpinner);
        existingAlbums = view.findViewById(R.id.existingAlbumsSpinner);

        Button importBtn = view.findViewById(R.id.confirmBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.optionList));
        optionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        option.setAdapter(optionAdapter);
        option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Existing Album")) {
                    existingAlbums.setEnabled(true);
                    input.setEnabled(false);
                } else {
                    existingAlbums.setEnabled(false);
                    input.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> albumsAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, LocalDataManager.getAlbumsNames());
        albumsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        existingAlbums.setAdapter(albumsAdapter);

        importBtn.setOnClickListener(view1 -> {
            if (option.getSelectedItem().toString().equals("Existing Album")) {//EXISTING ALBUMS
                if (existingAlbums != null && existingAlbums.getSelectedItem() != null) {
                    LocalDataManager.importImageToExistingOrNewAlbum(existingAlbums.getSelectedItem().toString(), imagesPath);
                    AlertDialog ad = new AlertDialog.Builder(getContext())
                            .setTitle("Import images to album successfully!!!")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .create();
                    ad.show();
                    Objects.requireNonNull(getDialog()).dismiss();
                } else {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage("No album exists!")
                            .setPositiveButton("GOT IT", null)
                            .show();
                }
            } else {//NEW ALBUMS
                if (input.getText().toString().equals("")) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage("The name of the new album cannot be empty!")
                            .setPositiveButton("GOT IT", null)
                            .show();
                } else {
                    String newAlbum = input.getText().toString();
                    if (LocalDataManager.getAlbumsNames().contains(newAlbum) || newAlbum.equalsIgnoreCase("FAVORITES")) {
                        new MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Error")
                                .setMessage("This album already exists!")
                                .setPositiveButton("GOT IT", null)
                                .show();
                    } else {
                        ArrayList<String> currentAlbums = LocalDataManager.getAlbumsNames();
                        currentAlbums.add(input.getText().toString());
                        LocalDataManager.setAlbumsNames(currentAlbums);
                        LocalDataManager.importImageToExistingOrNewAlbum(input.getText().toString(), imagesPath);
                        AlertDialog ad = new AlertDialog.Builder(getContext())
                                .setTitle("Import images to album successfully!!!")
                                .setPositiveButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .create();
                        ad.show();
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
    }
}
