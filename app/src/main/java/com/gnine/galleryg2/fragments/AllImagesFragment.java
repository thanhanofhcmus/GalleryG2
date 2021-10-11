package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.ImageRecyclerViewAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.tools.ImageLoader;

public class AllImagesFragment extends Fragment {

    public AllImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() == null) { return; }

        RecyclerView recyclerView = getView().findViewById(R.id.allPicturesFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        assert getActivity() != null;
        recyclerView.setAdapter(new ImageRecyclerViewAdapter(ImageLoader.loadImageFromSharedStorage(getActivity())));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }
}