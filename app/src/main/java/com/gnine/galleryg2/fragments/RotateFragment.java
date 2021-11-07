package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RotateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RotateFragment extends Fragment {


    public RotateFragment() {
        // Required empty public constructor
    }

    public static RotateFragment newInstance(String param1, String param2) {
        RotateFragment fragment = new RotateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate, container, false);
    }
}