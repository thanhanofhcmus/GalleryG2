package com.gnine.galleryg2.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.gnine.galleryg2.activities.FullImageActivity;
import com.gnine.galleryg2.activities.MainActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.SliderAdapter;
import com.gnine.galleryg2.data.ImageData;
import com.gnine.galleryg2.data.TrashData;
import com.gnine.galleryg2.tools.ContentHelper;
import com.gnine.galleryg2.tools.ImageSharer;
import com.gnine.galleryg2.tools.LocalDataManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class ViewPagerFragment extends Fragment {

    private ArrayList<ImageData> imageDataList;
    private ArrayList<Uri> favImages;
    private int position;
    private boolean isAlbum;
    private String albumTitle;
    private ViewPager2 viewPager2;
    private ArrayList<TrashData> trashList = null;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        assert getActivity() != null;

        Bundle bundle = requireActivity().getIntent().getExtras();
        imageDataList = bundle.getParcelableArrayList(MainActivity.IMAGE_LIST_KEY);
        favImages = LocalDataManager.getFavAlbum().stream()
                .map(imageData -> imageData.uri)
                .collect(Collectors.toCollection(ArrayList::new));
        position = FullImageActivity.getCurrentImagePosition();
        isAlbum = bundle.getBoolean(MainActivity.IS_ALBUM);
        albumTitle = bundle.getString(MainActivity.ALBUM_TITLE);

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }

        FullImageActivity.setIsInViewpagerFragment(true);

        trashList = LocalDataManager.getObjectListData("TRASH_LIST");

        view.findViewById(R.id.editBtn).setOnClickListener(v -> ((FullImageActivity)requireActivity()).startCrop(imageDataList.get(viewPager2.getCurrentItem()).uri));
        view.findViewById(R.id.delBtn).setOnClickListener(view1 -> {
            String sourcePath = imageDataList.get(viewPager2.getCurrentItem()).uri.getPath();
            if (isAlbum) {
                ArrayList<String> itemDel = new ArrayList<>();
                itemDel.add(sourcePath);
                LocalDataManager.removeImagesFromAlbum(albumTitle, itemDel);
                imageDataList.remove(imageDataList.get(viewPager2.getCurrentItem()));
            } else {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + ".nomedia");
                if (ContentHelper.moveFile(sourcePath, file.getPath())) {
                    imageDataList.remove(imageDataList.get(viewPager2.getCurrentItem()));
                    trashList.add(new TrashData(file.getPath(), sourcePath, 0));
                    LocalDataManager.setObjectListData("TRASH_LIST", trashList);
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Image is moved to trash")
                            .setPositiveButton("Cancel", ((dialog, which) -> { }))
                            .show();
                }
            }
            viewPager2.setAdapter(new SliderAdapter(imageDataList));
            new Handler().postDelayed(() -> viewPager2.setCurrentItem(position, false), 100);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getView() != null;

        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);
        viewPager2.setAdapter(new SliderAdapter(imageDataList));

        new Handler().postDelayed(() -> viewPager2.setCurrentItem(position, false), 100);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.full_activity_top_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem favItem = menu.findItem(R.id.action_favorite);
        if (favImages.contains(imageDataList.get(viewPager2.getCurrentItem()).uri)) {
            favItem.setIcon(R.drawable.ic_fill_favorite);
        } else {
            favItem.setIcon(R.drawable.ic_favorite);
        }
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (favImages.contains(imageDataList.get(position).uri)) {
                    favItem.setIcon(R.drawable.ic_fill_favorite);
                } else {
                    favItem.setIcon(R.drawable.ic_favorite);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            System.out.println(imageDataList.get(viewPager2.getCurrentItem()).uri);
            if (favImages.contains(imageDataList.get(viewPager2.getCurrentItem()).uri)) {//like -> unlike
                item.setIcon(R.drawable.ic_favorite);
                LocalDataManager.removeImageFromFav(imageDataList.get(viewPager2.getCurrentItem()).uri.getPath());
            } else {//unlike -> like
                item.setIcon(R.drawable.ic_fill_favorite);
                LocalDataManager.importImageIntoFav(imageDataList.get(viewPager2.getCurrentItem()).uri.getPath());
            }
            favImages = LocalDataManager.getFavAlbum().stream()
                    .map(imageData -> imageData.uri)
                    .collect(Collectors.toCollection(ArrayList::new));
            System.out.println(favImages);
        } else if (item.getItemId() == R.id.action_information) {
            int position = viewPager2.getCurrentItem();
            FullImageActivity.setCurrentImagePosition(position);
            FullImageActivity.setImageData(imageDataList.get(position));
            Navigation.findNavController(requireView()).navigate(R.id.viewPagerFragmentToInformationFragment);
        } else if (item.getItemId() == R.id.action_share) {
            ImageSharer.share(requireActivity(), imageDataList.get(viewPager2.getCurrentItem()));
        }
        return super.onOptionsItemSelected(item);
    }
}
