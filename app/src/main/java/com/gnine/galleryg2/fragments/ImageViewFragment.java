package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewFragment extends Fragment {

    public static ImageViewFragment newInstance(String param1, String param2) {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_view, container, false);
    }
}