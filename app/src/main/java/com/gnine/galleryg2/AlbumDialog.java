package com.gnine.galleryg2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class AlbumDialog extends DialogFragment {
    private static final String TAG = "AlbumDialog";

    private EditText input;
    private Button createBtn, cancelBtn;

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
        createBtn = view.findViewById(R.id.confirmBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);

        createBtn.setOnClickListener(view1 -> {
            if (!input.getText().toString().equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("newAlbum", input.getText().toString());
                this.getParentFragmentManager().setFragmentResult("result", bundle);
            }

            Objects.requireNonNull(getDialog()).dismiss();
        });
        cancelBtn.setOnClickListener(view1 -> {
            Objects.requireNonNull(getDialog()).dismiss();
        });
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
