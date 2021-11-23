package com.gnine.galleryg2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.gnine.galleryg2.activities.FullImageActivity;
import com.gnine.galleryg2.activities.MainActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.SliderAdapter;
import com.gnine.galleryg2.data.ImageData;

import java.util.ArrayList;
import java.util.Locale;


public class ViewPagerFragment extends Fragment {

    private ArrayList<ImageData> imageDataList;
    private int position;
    private ViewPager2 viewPager2;

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
        position = FullImageActivity.getCurrentImagePosition();

        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }

        FullImageActivity.setIsInViewpagerFragment(true);

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            Toast.makeText(getContext(), "like", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_information) {
            int position = viewPager2.getCurrentItem();
            FullImageActivity.setCurrentImagePosition(position);
            FullImageActivity.setImageData(imageDataList.get(position));
            Navigation.findNavController(requireView()).navigate(R.id.viewPagerFragmentToInformationFragment);
        } else if (item.getItemId() == R.id.action_share) {
            int currentPos = viewPager2.getCurrentItem();
            ImageData imageData = imageDataList.get(currentPos);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, imageData.uri);

            String imageName = imageData.name.toLowerCase(Locale.ROOT);

            if (imageName.endsWith(".jpeg") || imageName.endsWith("jpg")) {
                intent.setType("image/jpeg");
            } else if (imageName.endsWith(".png")) {
                intent.setType("image/png");
            } else if (imageName.endsWith(".gif")) {
                intent.setType("image/gif");
            } else {
                intent.setType("image/webp");
            }

            startActivity(Intent.createChooser(intent, null));
        }
        return super.onOptionsItemSelected(item);
    }
}
