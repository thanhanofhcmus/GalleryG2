package com.gnine.galleryg2.fragments;

import static com.gnine.galleryg2.adapters.ImageRecyclerViewAdapter.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.tools.ContentHelper;
import com.gnine.galleryg2.tools.ImageSharer;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.gnine.galleryg2.activities.MainActivity;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.adapters.ImageRecyclerViewAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.TrashData;
import com.gnine.galleryg2.tools.ImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public class AllImagesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter imageAdapter;
    private TextView textView;
    private int typeView;
    private int numImagesChecked;
    private ArrayList<ImageData> imageDataList = null;
    private boolean folder = false;
    private boolean types = false;
    private boolean albums = false;
    private String folderPath = null;
    private String typesTitle = null;
    private ArrayList<TrashData> trashList = null;

    private ActivityResultLauncher<String> cameraRequestLauncher;

    public AllImagesFragment() {
        // Required empty public constructor
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public void setAlbums(boolean albums) {
        this.albums = albums;
    }

    public void setTypes(boolean types) {
        this.types = types;
    }

    public void setTypesTitle(String typesTitle) {
        this.typesTitle = typesTitle;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setImageDataList(ArrayList<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    void update() {
        if (folder) {
            this.imageDataList = ImageLoader.getImagesFromFolder(folderPath);
        } else {
            this.imageDataList = (albums) ? LocalDataManager.getSingleAlbumData(folderPath) : ImageLoader.getAllImagesFromDevice();
        }
        if (this.imageDataList.size() > 0) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if (folder && !(new File(folderPath).exists())) {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            manager.popBackStack();
            FoldersFragment.checkBackPressed = true;
            FoldersFragment.tempFragment = null;
            BottomNavigationView bnv = requireActivity().findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(true);
        }
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
            numImagesChecked = 0;
            requireActivity().setTitle(String.valueOf(++numImagesChecked));
        };
        imageAdapter = new ImageRecyclerViewAdapter(imageDataList, onItemClick, onItemLongClick);
        imageAdapter.setState(ImageRecyclerViewAdapter.State.Normal);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        cameraRequestLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> { if (granted) { startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); }}
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setTitle("GalleryG2");
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
        if (getView() == null) {
            return;
        }

        trashList = LocalDataManager.getObjectListData("TRASH_LIST");

        if (folder || types || albums) {
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

        recyclerView = view.findViewById(R.id.allPicturesFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        typeView = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), typeView));

        textView = view.findViewById(R.id.allImagesFragmentEmpty);

        update();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_menu, menu);
        Activity activity = getActivity();
        assert activity != null;

        if (typeView == 2) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_2);
        } else if (typeView == 1) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_1);
        }

        if (imageAdapter.getState() == State.MultipleSelect) {
            menu.getItem(0).setVisible(true);
            if (albums)
                menu.getItem(1).setVisible(!folderPath.equals("Favorites"));
            else
                menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(false);
            activity.setTitle(String.valueOf(numImagesChecked));
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(true);
            menu.getItem(5).setVisible(true);
            if (folder || albums) {
                activity.setTitle(folderPath);
            } else if (types) {
                activity.setTitle(typesTitle);
            } else {
                activity.setTitle("GalleryG2");
            }
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
            } else if (typeView == 2) {
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
        } else if (item.getItemId() == R.id.menu_share) {
            ImageSharer.shareCheckedInList(requireActivity(), imageDataList);
        } else if (item.getItemId() == R.id.delete_images) {
            if (albums) {
                ArrayList<String> selectedImages = imageDataList.stream()
                        .filter(ImageData::isChecked)
                        .map(imageData -> imageData.uri.getPath())
                        .collect(Collectors.toCollection(ArrayList::new));
                if (selectedImages.isEmpty()) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage("Please select image(s) to remove from album!")
                            .setPositiveButton("GOT IT", null)
                            .show();
                } else
                    LocalDataManager.removeImagesFromAlbum(folderPath, selectedImages);
            } else
                AddToTrash();
            update();
            requireActivity().invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.select_all) {
            imageDataList.forEach(imageData -> imageData.setChecked(true));
            imageAdapter.setState(State.MultipleSelect);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            numImagesChecked = imageDataList.size();
            requireActivity().invalidateOptionsMenu();
            requireActivity().setTitle(String.valueOf(numImagesChecked));
        } else if (item.getItemId() == R.id.importAlbums) {
            ArrayList<String> selectedImages = imageDataList.stream()
                    .filter(ImageData::isChecked)
                    .map(imageData -> imageData.uri.getPath())
                    .collect(Collectors.toCollection(ArrayList::new));
            if (selectedImages.isEmpty()) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage("Please select image(s) to import!")
                        .setPositiveButton("GOT IT", null)
                        .show();
            } else {
                ImportDialog importDialog = new ImportDialog();
                importDialog.setData(selectedImages);
                importDialog.show(getParentFragmentManager(), "ImportDialog");
            }
        } else if (item.getItemId() == R.id.menu_camera) {
            startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        }

        return super.onOptionsItemSelected(item);
    }

    private void AddToTrash() {
        ArrayList<ImageData> selectedImages = imageDataList.stream()
                .filter(ImageData::isChecked)
                .collect(Collectors.toCollection(ArrayList::new));

        File file = new File(Environment.getExternalStorageDirectory() + "/" + ".nomedia");

        if ((file.exists() && file.isDirectory()) || file.mkdir()) {
            for (int i = 0; i < selectedImages.size(); i++) {
                String sourcePath = selectedImages.get(i).uri.getPath();
                if (ContentHelper.moveFile(sourcePath, file.getPath())) {
                    imageDataList.remove(selectedImages.get(i));
                    trashList.add(new TrashData(file.getPath(), sourcePath, 0));
                }
            }
        }
        LocalDataManager.setObjectListData("TRASH_LIST", trashList);
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Images are moved to trash")
                .setPositiveButton("Cancel", ((dialog, which) -> { }))
                .show();
    }

    private void sendImageListAndPositionToMain(int position) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;

        mainActivity.invokeFullImageActivity(imageDataList, position, albums, folderPath);
    }
}