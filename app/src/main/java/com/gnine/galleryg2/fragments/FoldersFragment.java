package com.gnine.galleryg2.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.adapters.FolderAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.TypeData;
import com.gnine.galleryg2.adapters.TypesAdapter;
import com.gnine.galleryg2.databinding.FragmentFoldersBinding;
import com.gnine.galleryg2.tools.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment {

    static Fragment tempFragment;

    static boolean checkBackPressed;

    private List<FolderData> folderList;

    public FoldersFragment() {
        // Required empty public constructor
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

        //Folders
        List<FolderData> folderDataList = getListFolders();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),
                folderDataList.size() > 4 ? 2 : 1, RecyclerView.HORIZONTAL, false);
        rcvFolder.setLayoutManager(gridLayoutManager);
        rcvFolder.setFocusable(false);
        rcvFolder.setNestedScrollingEnabled(false);

        FolderAdapter folderAdapter = new FolderAdapter();
        folderAdapter.setData(getListFolders());
        rcvFolder.setAdapter(folderAdapter);

        folderAdapter.setOnFolderClick(new FolderAdapter.FolderClick() {
            @Override
            public void onClick(View view, int position) {
                checkBackPressed = false;
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                tempFragment = new AllImagesFragment();
                ((AllImagesFragment)tempFragment).setFolder(true);
                ft.replace(R.id.fragmentFolders, tempFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //TypeData
        TypesAdapter typesAdapter = new TypesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvTypes.setLayoutManager(linearLayoutManager);
        rcvTypes.setFocusable(false);
        rcvTypes.setNestedScrollingEnabled(false);

        typesAdapter.setData(getListTypes());
        rcvTypes.setAdapter(typesAdapter);

        if (!checkBackPressed && tempFragment != null) { //not back from all images fragment of folder
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentFolders, tempFragment);
            ft.addToBackStack(null);
            ft.commit();
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
        list.add(new TypeData(R.drawable.ic_selfie, "Images", ImageLoader.loadImageFromSharedStorage(getActivity())));
        list.add(new TypeData(R.drawable.ic_screenshot, "Screenshots", null));

        return list;
    }

    private List<FolderData> getListFolders() {
        List<FolderData> list = new ArrayList<>();
        list.add(new FolderData(R.drawable.ic_folder, null,"All Images & Videos", ImageLoader.loadImageFromSharedStorage(getActivity())));
        return list;
    }
}