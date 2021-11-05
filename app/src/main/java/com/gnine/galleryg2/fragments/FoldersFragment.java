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

import com.gnine.galleryg2.Folder;
import com.gnine.galleryg2.FolderAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.Types;
import com.gnine.galleryg2.TypesAdapter;
import com.gnine.galleryg2.databinding.FragmentFoldersBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. Use the {@link FoldersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoldersFragment extends Fragment {

    FragmentFoldersBinding binding;

    private List<Folder> folderList;

    private RecyclerView rcvFolder;
    private RecyclerView rcvTypes;

    private FolderAdapter folderAdapter;
    private TypesAdapter typesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoldersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoldersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoldersFragment newInstance(String param1, String param2) {
        FoldersFragment fragment = new FoldersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {return;}
        rcvFolder = (RecyclerView) view.findViewById(R.id.rcv_folders);
        rcvTypes = (RecyclerView) view.findViewById(R.id.rcv_types);

        //Folders
        folderAdapter = new FolderAdapter();
        folderList = getListFolders();
        GridLayoutManager gridLayoutManager;
        if (folderList.size() > 4)
            gridLayoutManager = new GridLayoutManager(view.getContext(), 2, RecyclerView.HORIZONTAL, false);
        else
            gridLayoutManager = new GridLayoutManager(view.getContext(), 1, RecyclerView.HORIZONTAL, false);
        rcvFolder.setLayoutManager(gridLayoutManager);
        rcvFolder.setFocusable(false);
        rcvFolder.setNestedScrollingEnabled(false);

        folderAdapter.setData(getListFolders());
        rcvFolder.setAdapter(folderAdapter);

        folderAdapter.setOnFolderClick(new FolderAdapter.FolderClick() {
            @Override
            public void onClick(View view, int position) {

            }
        });

        //Types
        typesAdapter = new TypesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvTypes.setLayoutManager(linearLayoutManager);
        rcvTypes.setFocusable(false);
        rcvTypes.setNestedScrollingEnabled(false);

        typesAdapter.setData(getListTypes());
        rcvTypes.setAdapter(typesAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

    private List<Types> getListTypes() {
        List<Types> list = new ArrayList<>();
        list.add(new Types(R.drawable.ic_video, "Videos", null));
        list.add(new Types(R.drawable.ic_selfie, "Images", null));
        list.add(new Types(R.drawable.ic_screenshot, "Screenshots", null));

        return list;
    }

    private List<Folder> getListFolders() {
        List<Folder> list = new ArrayList<>();
        list.add(new Folder(R.drawable.ic_folder, null,"All Images & Videos", null));
        return list;
    }
}