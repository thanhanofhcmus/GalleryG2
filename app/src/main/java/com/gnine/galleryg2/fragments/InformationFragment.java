package com.gnine.galleryg2.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.gnine.galleryg2.FullImageActivity;
import com.gnine.galleryg2.R;
import com.gnine.galleryg2.data.ImageData;

public class InformationFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.information_layout, rootKey);
        ImageData imageData = FullImageActivity.getImageData();
        findPreference("Name").setSummary(imageData.name);
        findPreference("Path").setSummary(imageData.uri.getPath());
        findPreference("Size").setSummary(String.valueOf(imageData.size));
        findPreference("Date").setSummary(imageData.getDateTime());

        FullImageActivity.setIsInViewpagerFragment(false);
    }
}