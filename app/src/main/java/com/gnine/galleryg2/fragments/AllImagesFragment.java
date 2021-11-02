package com.gnine.galleryg2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.ImageData;
import com.gnine.galleryg2.ImageRecyclerViewAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.tools.ImageLoader;

import java.util.List;

public class AllImagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter imageAdapter;
    private int typeView;
    private int numImagesChecked;
    private List<ImageData> imageDataList;

    public AllImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        assert getActivity() != null;

        recyclerView = getView().findViewById(R.id.allPicturesFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        typeView = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), typeView));

        imageDataList = ImageLoader.loadImageFromSharedStorage(getActivity());

        imageAdapter = new ImageRecyclerViewAdapter(imageDataList);
        imageAdapter.setACTION_MODE(0);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemLongClickListener(new ImageRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view) {
                imageAdapter.setACTION_MODE(1);
                getActivity().invalidateOptionsMenu();
                imageAdapter.notifyDataSetChanged();
            }
        });

        imageAdapter.setOnItemClickListener(new ImageRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (imageAdapter.getACTION_MODE() == 1) {
                    if (!imageDataList.get(position).isChecked()) {
                        imageDataList.get(position).setChecked(true);
                        numImagesChecked++;
                        getActivity().setTitle(String.valueOf(numImagesChecked));
                    }
                    else {
                        imageDataList.get(position).setChecked(false);
                        numImagesChecked--;
                        getActivity().setTitle(String.valueOf(numImagesChecked));
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_menu, menu);

        if (typeView == 2) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_2);
        }
        else if (typeView == 1) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_1);
        }

        if (imageAdapter.getACTION_MODE() == 1) {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
            getActivity().setTitle(String.valueOf(numImagesChecked));
        }
        else {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
            getActivity().setTitle("HopeGallery6");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_chang_view) {
            if (typeView == 4) {
                typeView = 2;
                item.setIcon(R.drawable.ic_grid_2);
            }
            else if (typeView == 2 ) {
                typeView = 1;
                item.setIcon(R.drawable.ic_grid_1);
            }
            else {
                typeView = 4;
                item.setIcon(R.drawable.ic_grid_4);
            }
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), typeView));
        }
        else if (item.getItemId() == R.id.clear_choose) {
            imageAdapter.setACTION_MODE(0);
            imageAdapter.notifyDataSetChanged();
            numImagesChecked = 0;
            getActivity().invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }
}