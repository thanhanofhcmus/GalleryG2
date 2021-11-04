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
import com.gnine.galleryg2.slider.SliderAdapter;
import com.gnine.galleryg2.slider.SliderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerFragment newInstance(String param1, String param2) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ViewPager2 viewPager2= getView().findViewById(R.id.viewPagerImageSlider);
//        List<SliderItem> list=new ArrayList<>();
//        list.add(new SliderItem(R.drawable.image0));
//        list.add(new SliderItem(R.drawable.image1));
//        viewPager2.setAdapter(new SliderAdapter(list,viewPager2));
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
        ViewPager2 viewPager2= getView().findViewById(R.id.viewPagerImageSlider);
        List<SliderItem> list=new ArrayList<>();
        list.add(new SliderItem(R.drawable.image_0));
        list.add(new SliderItem(R.drawable.image_1));
        viewPager2.setAdapter(new SliderAdapter(list,viewPager2));
    }
}