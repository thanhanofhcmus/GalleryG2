package com.gnine.galleryg2.fragments;

import static com.gnine.galleryg2.adapters.ImageRecyclerViewAdapter.*;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnine.galleryg2.data.TimelineData;
import com.gnine.galleryg2.tools.ContentHelper;
import com.gnine.galleryg2.tools.ErrorDialog;
import com.gnine.galleryg2.tools.ImageSharer;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.gnine.galleryg2.activities.MainActivity;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.adapters.ImageRecyclerViewAdapter;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.TrashData;
import com.gnine.galleryg2.tools.ImageLoader;
import com.gnine.galleryg2.tools.PermissionChecker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public class AllImagesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageRecyclerViewAdapter imageAdapter;
    private TextView textView;
    private int typeView;
    private int numImagesChecked;
    private ArrayList<ImageData> imageDataList = null;
    private ArrayList<TimelineData> viewList = null;
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

    private void sortImageDataList() {
        this.imageDataList.sort((image1, image2) -> {
            String date1 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(image1.dateAdded);
            String date2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(image2.dateAdded);
            return date1.compareTo(date2) * (-1);
        });
    }

    public void setImageDataList(ArrayList<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
        sortImageDataList();
        toViewList();
    }

    private void update() {
        if (folder) {
            this.imageDataList = ImageLoader.getImagesFromFolder(folderPath);
        } else {
            this.imageDataList = albums ? LocalDataManager.getSingleAlbumData(folderPath) : ImageLoader.getAllImagesFromDevice();
        }
        sortImageDataList();
        toViewList();

        final FragmentActivity activity = requireActivity();

        if (this.imageDataList.size() > 0) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        if (folder && !(new File(folderPath).exists())) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.popBackStack();
            FoldersFragment.checkBackPressed = true;
            FoldersFragment.tempFragment = null;
            BottomNavigationView bnv = activity.findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(true);
        }

        BiConsumer<Integer, View> onItemClick = (position, view1) -> {
            if (imageAdapter.getState() == State.MultipleSelect) {
                if (!viewList.get(position).imageData.isChecked()) {
                    viewList.get(position).imageData.setChecked(true);
                    imageDataList.get(viewList.get(position).index).setChecked(true);
                    numImagesChecked++;
                } else {
                    viewList.get(position).imageData.setChecked(false);
                    imageDataList.get(viewList.get(position).index).setChecked(false);
                    numImagesChecked--;
                }
                activity.setTitle(String.valueOf(numImagesChecked));
                imageAdapter.notifyItemChanged(position);
            } else {
                sendImageListAndPositionToMain(viewList.get(position).index);
            }
        };

        BiConsumer<Integer, View> onItemLongClick = (position, view1) -> {
            imageAdapter.setState(State.MultipleSelect);
            activity.invalidateOptionsMenu();
            viewList.get(position).imageData.setChecked(true);
            imageDataList.get(viewList.get(position).index).setChecked(true);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            numImagesChecked = 0;
            activity.setTitle(String.valueOf(++numImagesChecked));
        };

        imageAdapter = new ImageRecyclerViewAdapter(viewList, onItemClick, onItemLongClick);
        imageAdapter.setState(ImageRecyclerViewAdapter.State.Normal);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        return inflater.inflate(R.layout.fragment_all_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() == null) {
            return;
        }

        trashList = LocalDataManager.getObjectListData(TrashFragment.TRASH_LIST_KEY);
        final FragmentActivity activity = requireActivity();

        cameraRequestLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> { if (granted) { startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); }}
        );

        if (folder || types || albums) {
            getView().setBackgroundColor(getResources().getColor(R.color.backgroundColor, requireContext().getTheme()));
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((view13, i, keyEvent) -> {
                if (i == KeyEvent.KEYCODE_BACK) {
                    FragmentManager manager = activity.getSupportFragmentManager();
                    manager.popBackStack();
                    FoldersFragment.checkBackPressed = true;
                    FoldersFragment.tempFragment = null;
                    BottomNavigationView bnv = activity.findViewById(R.id.bottomNavView);
                    bnv.getMenu().getItem(1).setEnabled(true);
                    return true;
                }
                return false;
            });
        } else if (!FoldersFragment.checkBackPressed && FoldersFragment.tempFragment != null) {
            BottomNavigationView bnv = activity.findViewById(R.id.bottomNavView);
            bnv.getMenu().getItem(1).setEnabled(true);
        }

        recyclerView = view.findViewById(R.id.allPicturesFragmentRecyclerView);
        recyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager(4);

        textView = view.findViewById(R.id.allImagesFragmentEmpty);

        update();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.appbar_menu, menu);
        Activity activity = requireActivity();

        if (typeView == 2) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_2);
        } else if (typeView == 1) {
            menu.getItem(1).setIcon(R.drawable.ic_grid_1);
        }

        if (imageAdapter.getState() == State.MultipleSelect) {
            menu.getItem(0).setVisible(true);
            if (albums) {
                menu.getItem(1).setVisible(!folderPath.equals("Favorites"));
            } else {
                menu.getItem(1).setVisible(true);
            }
            menu.getItem(2).setVisible(true);
            menu.getItem(3).setVisible(true);
            menu.getItem(4).setVisible(true);
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setVisible(true);
            menu.getItem(7).setVisible(false);
            activity.setTitle(String.valueOf(numImagesChecked));
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(true);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(true);
            menu.getItem(6).setVisible(true);
            menu.getItem(7).setVisible(true);
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
        final FragmentActivity activity = requireActivity();
        final Context context = requireContext();

        if (item.getItemId() == R.id.menu_chang_view) {
            if (typeView == 4) {
                setRecyclerViewLayoutManager(2);
                item.setIcon(R.drawable.ic_grid_2);
            } else if (typeView == 2) {
                setRecyclerViewLayoutManager(1);
                item.setIcon(R.drawable.ic_grid_1);
            } else {
                setRecyclerViewLayoutManager(4);
                item.setIcon(R.drawable.ic_grid_4);
            }
        } else if (item.getItemId() == R.id.clear_choose) {
            imageAdapter.setState(State.Normal);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            numImagesChecked = 0;
            activity.invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.menu_share) {
            ImageSharer.shareCheckedInList(activity, imageDataList);
        } else if (item.getItemId() == R.id.delete_images) {
            if (albums) {
                ArrayList<String> selectedImages = imageDataList.stream()
                        .filter(ImageData::isChecked)
                        .map(imageData -> imageData.uri.getPath())
                        .collect(Collectors.toCollection(ArrayList::new));
                if (selectedImages.isEmpty()) {
                    ErrorDialog.show(context, "Please select image(s) to remove from album!");
                } else {
                    LocalDataManager.removeImagesFromAlbum(folderPath, selectedImages);
                }
            } else {
                addToTrash();
            }
            update();
            activity.invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.select_all) {
            imageDataList.forEach(imageData -> imageData.setChecked(true));
            imageAdapter.setState(State.MultipleSelect);
            imageAdapter.notifyItemRangeChanged(0, imageAdapter.getItemCount());
            numImagesChecked = imageDataList.size();
            activity.invalidateOptionsMenu();
            activity.setTitle(String.valueOf(numImagesChecked));
        } else if (item.getItemId() == R.id.importAlbums) {
            ArrayList<String> selectedImages = imageDataList.stream()
                    .filter(ImageData::isChecked)
                    .map(imageData -> imageData.uri.getPath())
                    .collect(Collectors.toCollection(ArrayList::new));
            if (selectedImages.isEmpty()) {
                ErrorDialog.show(context, "Please select image(s) to import!");
            } else {
                ImportDialog importDialog = new ImportDialog(selectedImages, requireView());
                importDialog.show(getParentFragmentManager(), "ImportDialog");
            }
        } else if (item.getItemId() == R.id.menu_camera) {
            startCamera();
        } else if (item.getItemId() == R.id.slideShow) {
            if (imageAdapter.getState() == State.MultipleSelect) {
                ArrayList<ImageData> selectedImages = imageDataList.stream()
                        .filter(ImageData::isChecked)
                        .collect(Collectors.toCollection(ArrayList::new));
                if (selectedImages.isEmpty()) {
                    ErrorDialog.show(context, "Please select image(s) to make a slideshow!");
                } else {
                    SlideshowDialog ssDialog = new SlideshowDialog(selectedImages);
                    ssDialog.show(getParentFragmentManager(), "SlideShowDialog");
                }
            } else {
                SlideshowDialog ssDialog = new SlideshowDialog(imageDataList);
                ssDialog.show(getParentFragmentManager(), "SlideShowDialog");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void toViewList(){
        if (imageDataList.size() > 0) {
            viewList = new ArrayList<>();
            String date = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(imageDataList.get(0).dateAdded);
            date += '.';
            for (int i = 0; i < imageDataList.size(); i++) {
                String dateAdded = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(imageDataList.get(i).dateAdded);
                if (!dateAdded.equals(date)) {
                    date = dateAdded;
                    viewList.add(new TimelineData(TimelineData.Type.Time, date, imageDataList.get(i), i));
                }
                viewList.add(new TimelineData(TimelineData.Type.Image, "", imageDataList.get(i), i));
            }
        }
    }

    private void setRecyclerViewLayoutManager(int newTypeView) {
        typeView = newTypeView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), typeView);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return viewList.get(position).type == TimelineData.Type.Time ? typeView : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void addToTrash() {
        ArrayList<ImageData> selectedImages = imageDataList.stream()
                .filter(ImageData::isChecked)
                .collect(Collectors.toCollection(ArrayList::new));

        File file = new File(Environment.getExternalStorageDirectory() + "/" + ".nomedia");

        if ((file.exists() && file.isDirectory()) || file.mkdir()) {
            for (ImageData image : selectedImages) {
                String sourcePath = image.uri.getPath();
                if (ContentHelper.moveFile(sourcePath, file.getPath())) {
                    imageDataList.remove(image);
                    trashList.add(new TrashData(file.getPath(), sourcePath, 0));
                }
            }
        }
        LocalDataManager.setObjectListData(TrashFragment.TRASH_LIST_KEY, trashList);
        Snackbar.make(requireView(), "Images removed", Snackbar.LENGTH_SHORT).show();
    }

    private void sendImageListAndPositionToMain(int position) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;

        mainActivity.invokeFullImageActivity(imageDataList, position, albums, folderPath);
    }

    void startCamera() {
        try {
            final String permission = Manifest.permission.CAMERA;
            if (! PermissionChecker.checkPermission(this, permission)) {
                cameraRequestLauncher.launch(permission);
            } else {
                startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        } catch (Exception e) {
            Log.e("CAMERA", e.getMessage());
        }
    }
}