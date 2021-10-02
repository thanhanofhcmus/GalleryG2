package com.gnine.galleryg2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AllPicturesFragment extends Fragment {


    public AllPicturesFragment() {
        // Required empty public constructor
    }

    public static AllPicturesFragment newInstance(String param1, String param2) {
        return new AllPicturesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_pictures, container, false);
    }
}