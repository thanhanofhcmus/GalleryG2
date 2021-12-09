package com.gnine.galleryg2.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnine.galleryg2.tools.ContentHelper;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.TrashAdapter;
import com.gnine.galleryg2.data.TrashData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public class TrashFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private TrashAdapter imageAdapter;
    private int numImagesChecked;
    private ArrayList<TrashData> trashList = null;

    public TrashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void update() {
        BiConsumer<Integer, View> onItemClick = (position, view12) -> {
            if (imageAdapter.getState() == TrashAdapter.State.MultipleSelect) {
                if (!trashList.get(position).isChecked()) {
                    trashList.get(position).setChecked(true);
                    numImagesChecked++;
                } else {
                    trashList.get(position).setChecked(false);
                    numImagesChecked--;
                }
                requireActivity().setTitle(String.valueOf(numImagesChecked));
                imageAdapter.notifyItemChanged(position);
            }
        };

        BiConsumer<Integer, View> onItemLongClick = (position, view1) -> {
            imageAdapter.setState(TrashAdapter.State.MultipleSelect);
            requireActivity().invalidateOptionsMenu();
            trashList.get(position).setChecked(true);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            numImagesChecked = 0;
            requireActivity().setTitle(String.valueOf(++numImagesChecked));
        };

        imageAdapter = new TrashAdapter(trashList, onItemClick, onItemLongClick);
        imageAdapter.setState(TrashAdapter.State.Normal);
        recyclerView.setAdapter(imageAdapter);
    }

    private void resume() {
        if (trashList.size() > 0) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            update();
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() == null) {
            return;
        }

        trashList = LocalDataManager.getObjectListData("TRASH_LIST");

        textView = view.findViewById(R.id.trashFragmentEmpty);
        //textView.setVisibility(View.GONE);

        if (!FoldersFragment.checkBackPressed && FoldersFragment.tempFragment != null) {
            BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(true);
        }

        recyclerView = view.findViewById(R.id.trashFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        if (trashList.size() > 0) {
            update();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.trash_menu, menu);
        Activity activity = getActivity();
        assert activity != null;

        if (trashList.size() > 0 && imageAdapter.getState() == TrashAdapter.State.MultipleSelect) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(false);
            activity.setTitle(String.valueOf(numImagesChecked));
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(true);
            activity.setTitle("GalleryG2");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        assert getActivity() != null;
        if (trashList.size() > 0) {
            if (item.getItemId() == R.id.clear_choose) {
                imageAdapter.setState(TrashAdapter.State.Normal);
                imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
                numImagesChecked = 0;
                getActivity().invalidateOptionsMenu();
            } else if (item.getItemId() == R.id.restore_images) {
                RestoreToTrash();
                resume();
            } else if (item.getItemId() == R.id.delete_images) {
                deleteToTrash();
                resume();
            } else if (item.getItemId() == R.id.select_all) {
                for (int i = 0; i < trashList.size(); i++) {
                    trashList.get(i).setChecked(true);
                }
                imageAdapter.setState(TrashAdapter.State.MultipleSelect);
                imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
                requireActivity().invalidateOptionsMenu();
                numImagesChecked = trashList.size();
                requireActivity().setTitle(String.valueOf(numImagesChecked));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteToTrash() {
        ArrayList<TrashData> selectedTrash = trashList.stream()
                .filter(TrashData::isChecked)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < selectedTrash.size(); i++) {
            int index = selectedTrash.get(i).oldPath.lastIndexOf("/");
            String name = selectedTrash.get(i).oldPath.substring(index + 1);
            File file = new File(selectedTrash.get(i).trashPath + "/" + name);

            if (deleteFile(file.getPath())) {
                trashList.remove(selectedTrash.get(i));
            }
        }
        LocalDataManager.setObjectListData("TRASH_LIST", trashList);
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Images are permanently deleted")
                .setPositiveButton("Cancel", ((dialog, which) -> { }))
                .show();

    }

    private boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    private void RestoreToTrash() {
        ArrayList<TrashData> selectedTrash = trashList.stream()
                .filter(TrashData::isChecked)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < selectedTrash.size(); i++) {
            int index = selectedTrash.get(i).oldPath.lastIndexOf("/");
            String name = selectedTrash.get(i).oldPath.substring(index + 1);
            String targetPath = selectedTrash.get(i).oldPath.substring(0, index);
            File file = new File(selectedTrash.get(i).trashPath + "/" + name);

            if (ContentHelper.moveFile(file.getPath(), targetPath)) {
                trashList.remove(selectedTrash.get(i));
            }
        }
        LocalDataManager.setObjectListData("TRASH_LIST", trashList);
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Images are restored")
                .setPositiveButton("Cancel", ((dialog, which) -> { }))
                .show();
    }
}