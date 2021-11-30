package com.gnine.galleryg2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gnine.galleryg2.R;

import java.util.Objects;

public class ImportDialog extends DialogFragment {
    private EditText input;
    private Spinner option, existingAlbums;

    public ImportDialog() {}

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

        importBtn.setOnClickListener(view1 -> {});
        cancelBtn.setOnClickListener(view1 -> Objects.requireNonNull(getDialog()).dismiss());
    }
}
