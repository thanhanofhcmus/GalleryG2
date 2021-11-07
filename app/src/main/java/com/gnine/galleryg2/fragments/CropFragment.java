package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.R;

public class CropFragment extends Fragment {

    public CropFragment() {
        // Required empty public constructor
    }

    public static CropFragment newInstance(String param1, String param2) {
        CropFragment fragment = new CropFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop, container, false);
    }
}