package com.gnine.galleryg2.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment {

    FragmentFoldersBinding binding;

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

        //TypeData
        TypesAdapter typesAdapter = new TypesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvTypes.setLayoutManager(linearLayoutManager);
        rcvTypes.setFocusable(false);
        rcvTypes.setNestedScrollingEnabled(false);

        typesAdapter.setData(getListTypes());
        rcvTypes.setAdapter(typesAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFoldersBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private List<TypeData> getListTypes() {
        List<TypeData> list = new ArrayList<>();
        list.add(new TypeData(R.drawable.ic_video, "Videos", null));
        list.add(new TypeData(R.drawable.ic_selfie, "Images", null));
        list.add(new TypeData(R.drawable.ic_screenshot, "Screenshots", null));

        return list;
    }

    private List<FolderData> getListFolders() {
        List<FolderData> list = new ArrayList<>();
        list.add(new FolderData(R.drawable.ic_folder, null,"All Images & Videos", null));
        return list;
    }
}