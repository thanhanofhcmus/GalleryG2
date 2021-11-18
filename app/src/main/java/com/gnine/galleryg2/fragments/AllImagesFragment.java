package com.gnine.galleryg2.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.MainActivity;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.adapters.ImageRecyclerViewAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.tools.ImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class AllImagesFragment extends Fragment {

    public enum State {
        Normal,
        MultipleSelect
    }

    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter imageAdapter;
    private int typeView;
    private int numImagesChecked;
    private ArrayList<ImageData> imageDataList = null;
    private boolean folder;


    public AllImagesFragment() {
        // Required empty public constructor
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public void setImageDataList(ArrayList<ImageData> imageDataList) {this.imageDataList = imageDataList;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.imageDataList = (folder) ? imageDataList : ImageLoader.loadImageFromSharedStorage(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.imageDataList = (folder) ? imageDataList : ImageLoader.loadImageFromSharedStorage(requireActivity());
        BiConsumer<Integer, View> onItemClick = (position, view12) -> {
            if (imageAdapter.getState() == State.MultipleSelect) {
                if (!imageDataList.get(position).isChecked()) {
                    imageDataList.get(position).setChecked(true);
                    numImagesChecked++;
                } else {
                    imageDataList.get(position).setChecked(false);
                    numImagesChecked--;
                }
                requireActivity().setTitle(String.valueOf(numImagesChecked));
                imageAdapter.notifyItemChanged(position);
            } else {
                sendImageListAndPositionToMain(position);
            }
        };

        BiConsumer<Integer, View> onItemLongClick = (position, view1) -> {
            imageAdapter.setState(State.MultipleSelect);
            requireActivity().invalidateOptionsMenu();
            imageDataList.get(position).setChecked(true);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            requireActivity().setTitle(String.valueOf(++numImagesChecked));
        };
        imageAdapter = new ImageRecyclerViewAdapter(imageDataList, onItemClick, onItemLongClick);
        recyclerView.setAdapter(imageAdapter);
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

        if (folder) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((view13, i, keyEvent) -> {
                if (i == KeyEvent.KEYCODE_BACK) {
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    manager.popBackStack();
                    FoldersFragment.checkBackPressed = true;
                    FoldersFragment.tempFragment = null;
                    BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
                    bnv.getMenu().getItem(1).setEnabled(true);
                    return true;
                }
                return false;
            });
        } else if (!FoldersFragment.checkBackPressed && FoldersFragment.tempFragment != null) {
            BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(true);
        }

        recyclerView = getView().findViewById(R.id.allPicturesFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        typeView = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), typeView));

        BiConsumer<Integer, View> onItemClick = (position, view12) -> {
            if (imageAdapter.getState() == State.MultipleSelect) {
                if (!imageDataList.get(position).isChecked()) {
                    imageDataList.get(position).setChecked(true);
                    numImagesChecked++;
                } else {
                    imageDataList.get(position).setChecked(false);
                    numImagesChecked--;
                }
                requireActivity().setTitle(String.valueOf(numImagesChecked));
                imageAdapter.notifyItemChanged(position);
            } else {
                sendImageListAndPositionToMain(position);
            }
        };

        BiConsumer<Integer, View> onItemLongClick = (position, view1) -> {
            imageAdapter.setState(State.MultipleSelect);
            requireActivity().invalidateOptionsMenu();
            imageDataList.get(position).setChecked(true);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            requireActivity().setTitle(String.valueOf(++numImagesChecked));
        };

        imageAdapter = new ImageRecyclerViewAdapter(imageDataList, onItemClick, onItemLongClick);
        imageAdapter.setState(State.Normal);
        recyclerView.setAdapter(imageAdapter);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_menu, menu);
        Activity activity = getActivity();
        assert activity != null;

        if (typeView == 2) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_2);
        }
        else if (typeView == 1) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_1);
        }

        if (imageAdapter.getState() == State.MultipleSelect) {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
            activity.setTitle(String.valueOf(numImagesChecked));
        } else {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
            activity.setTitle("GalleryG2");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        assert getActivity() != null;
        if (item.getItemId() == R.id.menu_chang_view) {
            if (typeView == 4) {
                typeView = 2;
                item.setIcon(R.drawable.ic_grid_2);
            } else if (typeView == 2 ) {
                typeView = 1;
                item.setIcon(R.drawable.ic_grid_1);
            } else {
                typeView = 4;
                item.setIcon(R.drawable.ic_grid_4);
            }
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), typeView));
        } else if (item.getItemId() == R.id.clear_choose) {
            imageAdapter.setState(State.Normal);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            numImagesChecked = 0;
            getActivity().invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendImageListAndPositionToMain(int position) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.invokeFullImageActivity(imageDataList, position);
    }
}