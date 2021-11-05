package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnine.galleryg2.R;
import com.gnine.galleryg2.adapters.SliderAdapter;
import com.gnine.galleryg2.tools.SliderItem;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerFragment extends Fragment {

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_view_pager,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        view.findViewById(R.id.editBtn).setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.viewPagerToEditFragment));
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getView() != null;
        ViewPager2 viewPager2= getView().findViewById(R.id.viewPagerImageSlider);
        List<SliderItem> list=new ArrayList<>();
        list.add(new SliderItem(R.drawable.image_0));
        list.add(new SliderItem(R.drawable.image_1));
        viewPager2.setAdapter(new SliderAdapter(list,viewPager2));
    }
}