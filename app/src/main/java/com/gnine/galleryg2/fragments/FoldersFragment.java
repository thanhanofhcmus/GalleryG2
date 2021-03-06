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

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.FolderAdapter;
import com.gnine.galleryg2.adapters.TypesAdapter;
import com.gnine.galleryg2.data.FolderData;
import com.gnine.galleryg2.data.TypeData;
import com.gnine.galleryg2.tools.ImageLoader;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class FoldersFragment extends Fragment {
    static Fragment tempFragment;

    static boolean checkBackPressed;

    List<FolderData> folderDataList;
    List<TypeData> typeDataList;
    List<FolderData> albumsList;
    FolderAdapter folderAdapter;
    FolderAdapter albumAdapter;
    TypesAdapter typesAdapter;
    RecyclerView rcvFolder, rcvTypes, rcvAlbums;
    GridLayoutManager gridLayoutManager, albumLayoutManager;
    BiConsumer<Integer, View> onFolderClick;
    BiConsumer<Integer, View> onAlbumClick;
    BiConsumer<Integer, View> onTypeClick;

    public FoldersFragment() {
        // Required empty public constructor
    }

    void update() {
        folderDataList = getListFolders();
        typeDataList = getListTypes();
        albumsList = getAlbumsList();
        onFolderClick = (position, view1) -> {
            checkBackPressed = false;
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            tempFragment = new AllImagesFragment();
            ((AllImagesFragment) tempFragment).setFolder(true);
            ((AllImagesFragment) tempFragment).setFolderPath(folderDataList.get(position).uri.getPath());
            ((AllImagesFragment) tempFragment).setImageDataList(folderDataList.get(position).imageList);
            BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(false);
            ft.replace(R.id.fragmentFolders, tempFragment);
            ft.addToBackStack(null);
            ft.commit();
        };

        onAlbumClick = (position, view1) -> {
            if (albumAdapter.getState() == FolderAdapter.State.Normal) {
                checkBackPressed = false;
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                tempFragment = new AllImagesFragment();
                ((AllImagesFragment) tempFragment).setAlbums(true);
                ((AllImagesFragment) tempFragment).setFolderPath(albumsList.get(position).title);
                ((AllImagesFragment) tempFragment).setImageDataList(albumsList.get(position).imageList);
                BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
                bnv.getMenu().getItem(1).setEnabled(false);
                ft.replace(R.id.fragmentFolders, tempFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                if (getAlbumsList().get(position).title.equals("Favorites")) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage("You cannot delete this album!")
                            .setPositiveButton("GOT IT", null)
                            .show();
                } else {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("WARNING")
                            .setMessage("Do you really want to delete this album?")
                            .setNegativeButton("CANCEL", null)
                            .setPositiveButton("YES", (dialog, which) -> {
                                FolderData targetAlbumDel = albumsList.get(position);
                                LocalDataManager.removeSingleAlbum(targetAlbumDel.title);
                                albumsList.remove(targetAlbumDel);
                                albumAdapter.notifyItemRemoved(position);
                            })
                            .show();
                }
            }

        };

        onTypeClick = (position, view1) -> {
            if (getListTypes().get(position).list != null) {
                checkBackPressed = false;
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                tempFragment = new AllImagesFragment();
                ((AllImagesFragment) tempFragment).setTypes(true);
                ((AllImagesFragment) tempFragment).setTypesTitle(typeDataList.get(position).title);
                ((AllImagesFragment) tempFragment).setImageDataList(typeDataList.get(position).list);
                BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
                bnv.getMenu().getItem(1).setEnabled(false);
                ft.replace(R.id.fragmentFolders, tempFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        };

        gridLayoutManager = new GridLayoutManager(requireActivity(),
                folderDataList.size() > 4 ? 2 : 1, RecyclerView.HORIZONTAL, false);
        rcvFolder.setLayoutManager(gridLayoutManager);
        folderAdapter = new FolderAdapter(folderDataList, onFolderClick);
        folderAdapter.setData(folderDataList);
        rcvFolder.setAdapter(folderAdapter);

        albumLayoutManager = new GridLayoutManager(requireActivity(),
                albumsList.size() > 4 ? 2 : 1, RecyclerView.HORIZONTAL, false);
        rcvAlbums.setLayoutManager(albumLayoutManager);
        albumAdapter = new FolderAdapter(albumsList, onAlbumClick);
        albumAdapter.setData(albumsList);
        rcvAlbums.setAdapter(albumAdapter);

        typesAdapter = new TypesAdapter(typeDataList, onTypeClick);
        typesAdapter.setData(typeDataList);
        rcvTypes.setAdapter(typesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvFolder = view.findViewById(R.id.rcv_folders);
        rcvTypes = view.findViewById(R.id.rcv_types);
        rcvAlbums = view.findViewById(R.id.rcv_albums);

        update();

        //Folder

        rcvFolder.setFocusable(false);
        rcvFolder.setNestedScrollingEnabled(false);

        //Albums

        rcvAlbums.setFocusable(false);
        rcvAlbums.setNestedScrollingEnabled(false);

        //TypeData

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvTypes.setLayoutManager(linearLayoutManager);
        rcvTypes.setFocusable(false);
        rcvTypes.setNestedScrollingEnabled(false);

        ImageButton addingBtn = view.findViewById(R.id.addingBtn);
        addingBtn.setOnClickListener(view1 -> {
            AlbumDialog ad = new AlbumDialog(requireView());
            ad.setOnDismissListener(dialogInterface -> update());
            ad.show(getParentFragmentManager(), "AlbumDialog");
        });

        ImageButton editingBtn = view.findViewById(R.id.editingBtn);
        editingBtn.setOnClickListener(view1 -> {
            if (albumAdapter.getState() == FolderAdapter.State.Normal)
                albumAdapter.setState(FolderAdapter.State.Edit);
            else
                albumAdapter.setState(FolderAdapter.State.Normal);
            albumAdapter.notifyItemRangeChanged(0, albumAdapter.getItemCount());
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

    private List<TypeData> getListTypes() {
        List<TypeData> list = new ArrayList<>();
        list.add(new TypeData(R.drawable.ic_video, "Videos", null));
        list.add(new TypeData(R.drawable.ic_selfie, "Images",
                ImageLoader.getAllImagesFromDevice()));
        list.add(new TypeData(R.drawable.ic_screenshot, "Screenshots", null));

        return list;
    }

    private List<FolderData> getListFolders() {
        ImageLoader.retrieveFoldersHaveImage("/storage/");
        List<FolderData> list = new ArrayList<>(Objects.requireNonNull(ImageLoader.retrieveFoldersHaveImage("/storage/")));
        ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath());
        list.addAll(Objects.requireNonNull(ImageLoader.retrieveFoldersHaveImage(Environment.getExternalStorageDirectory().getPath())));
        return list;
    }

    private List<FolderData> getAlbumsList() {
        ArrayList<FolderData> list = new ArrayList<>();
        list.add(new FolderData(R.drawable.ic_folder, null, "Favorites", LocalDataManager.getFavAlbum()));
        list.addAll(LocalDataManager.getAllAlbumsData());
        return list;
    }
}