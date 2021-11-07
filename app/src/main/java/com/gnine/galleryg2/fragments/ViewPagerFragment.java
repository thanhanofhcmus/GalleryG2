package com.gnine.galleryg2.fragments;

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
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.FullImageActivity;
import com.gnine.galleryg2.MainActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.SliderAdapter;
import com.gnine.galleryg2.data.ImageData;

import java.util.ArrayList;


public class ViewPagerFragment extends Fragment {

    private ArrayList<ImageData> imageDataList;
    private int position;

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
        position = bundle.getInt(MainActivity.IMAGE_POSITION_KEY);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) { actionBar.show(); }

        view.findViewById(R.id.editBtn).setOnClickListener(
                v ->{
                    ViewPager2 viewPager2 = getView().findViewById(R.id.viewPagerImageSlider);
                    int pos=viewPager2.getCurrentItem();
                    Bundle _bundle=new Bundle();
                    _bundle.putParcelable(FullImageActivity.IMAGE_DATA_KEY,imageDataList.get(pos));
                    Navigation.findNavController(view).navigate(R.id.viewPagerToEditFragment,_bundle);
                });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getView() != null;

        ViewPager2 viewPager2 = getView().findViewById(R.id.viewPagerImageSlider);
        viewPager2.setAdapter(new SliderAdapter(imageDataList));
        new Handler().postDelayed(() -> viewPager2.setCurrentItem(position, false), 100);
    }

}