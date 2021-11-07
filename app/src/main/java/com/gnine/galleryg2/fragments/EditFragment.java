package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import com.gnine.galleryg2.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class EditFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
//        Toolbar toolbar=getView().findViewById(R.id.toolbar);
        RoundedImageView imageView = view.findViewById(R.id.singleImage);
        Button closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(
                v -> Navigation.findNavController(view).navigate(R.id.editToViewPagerFragment));
        final float[] fromRotation = {0};
        final float[] toRotation = {90};
        Button editBtn = view.findViewById(R.id.rotateBtn);
        editBtn.setOnClickListener(v -> {
            final RotateAnimation rotateAnim = new RotateAnimation(
                    fromRotation[0], toRotation[0], RotateAnimation.RELATIVE_TO_SELF, .5f,
                    RotateAnimation.RELATIVE_TO_SELF, .5f);

            rotateAnim.setDuration(1000); // Use 0 ms to rotate instantly
            rotateAnim.setFillAfter(true); // Must be true or the animation will reset
            imageView.startAnimation(rotateAnim);
            fromRotation[0] += 90;
            toRotation[0] += 90;
        });
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert  actionBar != null;
        actionBar.hide();

        return view;
    }
}