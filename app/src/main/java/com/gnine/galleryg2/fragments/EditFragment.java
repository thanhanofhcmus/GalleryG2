package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Toolbar;

import com.gnine.galleryg2.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_edit,container,false);
//        Toolbar toolbar=getView().findViewById(R.id.toolbar);
        RoundedImageView imageView=view.findViewById(R.id.singleImage);
        Button closeBtn=view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.editToViewPagerFragment));
        final float[] fromRotation = {0};
        final float[] toRotation = {90};
        Button editBtn=view.findViewById(R.id.rotateBtn);
        editBtn.setOnClickListener(v->{
            final RotateAnimation rotateAnim = new RotateAnimation(
                    fromRotation[0], toRotation[0], RotateAnimation.RELATIVE_TO_SELF,.5f, RotateAnimation.RELATIVE_TO_SELF,.5f);

            rotateAnim.setDuration(1000); // Use 0 ms to rotate instantly
            rotateAnim.setFillAfter(true); // Must be true or the animation will reset
            imageView.startAnimation(rotateAnim);
            fromRotation[0] +=90;
            toRotation[0] +=90;
        });
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return view;
    }
}