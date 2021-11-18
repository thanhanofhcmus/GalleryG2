package com.gnine.galleryg2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.AlbumDialog;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.FolderAdapter;
import com.gnine.galleryg2.adapters.TypesAdapter;
import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.TypeData;
import com.gnine.galleryg2.tools.ImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class FoldersFragment extends Fragment {

    static Fragment tempFragment;

    static boolean checkBackPressed;

    List<FolderData> folderDataList;
    List<TypeData> typeDataList;

    public FoldersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folderDataList = getListFolders();
        typeDataList = getListTypes();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rcvFolder = view.findViewById(R.id.rcv_folders);
        RecyclerView rcvTypes = view.findViewById(R.id.rcv_types);
        RecyclerView rcvAlbums = view.findViewById(R.id.rcv_albums);

        //Folder
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),
                folderDataList.size() > 4 ? 2 : 1, RecyclerView.HORIZONTAL, false);
        rcvFolder.setLayoutManager(gridLayoutManager);
        rcvFolder.setFocusable(false);
        rcvFolder.setNestedScrollingEnabled(false);

        BiConsumer<Integer, View> onFolderClick = (position, view1) -> {
            checkBackPressed = false;
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            tempFragment = new AllImagesFragment();
            ((AllImagesFragment) tempFragment).setFolder(true);
            ((AllImagesFragment) tempFragment).setImageDataList(getListFolders().get(position).imageList);
            BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(false);
            ft.replace(R.id.fragmentFolders, tempFragment);
            ft.addToBackStack(null);
            ft.commit();
        };

        BiConsumer<Integer, View> onTypeClick = (position, view1) -> {
            if (getListTypes().get(position).list != null) {
                checkBackPressed = false;
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                tempFragment = new AllImagesFragment();
                ((AllImagesFragment) tempFragment).setFolder(true);
                ((AllImagesFragment) tempFragment).setImageDataList(getListTypes().get(position).list);
                BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
                bnv.getMenu().getItem(1).setEnabled(false);
                ft.replace(R.id.fragmentFolders, tempFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        };

        FolderAdapter folderAdapter = new FolderAdapter(folderDataList, onFolderClick);
        folderAdapter.setData(folderDataList);
        rcvFolder.setAdapter(folderAdapter);

        //TypeData
        TypesAdapter typesAdapter = new TypesAdapter(typeDataList, onTypeClick);
        typesAdapter.setData(typeDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvTypes.setLayoutManager(linearLayoutManager);
        rcvTypes.setFocusable(false);
        rcvTypes.setNestedScrollingEnabled(false);
        rcvTypes.setAdapter(typesAdapter);

        ImageButton addingBtn = (ImageButton) view.findViewById(R.id.addingBtn);
        addingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumDialog ad = new AlbumDialog(requireActivity());
                ad.show();
            }
        });

        if (!checkBackPressed && tempFragment != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(false);
            ft.replace(R.id.fragmentFolders, tempFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            tempFragment = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folders, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private List<TypeData> getListTypes() {
        List<TypeData> list = new ArrayList<>();
        list.add(new TypeData(R.drawable.ic_video, "Videos", null));
        list.add(new TypeData(R.drawable.ic_selfie, "Images",
                ImageLoader.getAllImagesFromDevice(requireActivity())));
        list.add(new TypeData(R.drawable.ic_screenshot, "Screenshots", null));

        return list;
    }

    private List<FolderData> getListFolders() {
        List<FolderData> list = new ArrayList<>();
        if (ImageLoader.retrieveFoldersHaveImage("/storage/") != null)
            list.addAll(Objects.requireNonNull(ImageLoader.retrieveFoldersHaveImage("/storage/")));
        if (ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath()) != null)
            list.addAll(Objects.requireNonNull(ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath())));
        return list;
    }
}