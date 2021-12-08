package com.gnine.galleryg2.tools;

import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.gnine.galleryg2.BuildConfig;
import com.gnine.galleryg2.data.ImageData;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class ImageSharer {
    public static void share(final FragmentActivity activity, final ImageData imageData) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(activity, imageData.uri));

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

        activity.startActivity(Intent.createChooser(intent, null));
    }

    public static void shareCheckedInList(final FragmentActivity activity, final ArrayList<ImageData> list) {
        final ArrayList<Uri> checkedUriList = list.stream()
                .filter(ImageData::isChecked)
                .map(imageData -> getUriForFile(activity, imageData.uri))
                .collect(Collectors.toCollection(ArrayList::new));
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, checkedUriList);
        intent.setType("image/*");
        activity.startActivity(Intent.createChooser(intent, null));
    }

    public static Uri getUriForFile(FragmentActivity activity, Uri uri) {
        return FileProvider.getUriForFile(
                activity,
                BuildConfig.APPLICATION_ID + "." + activity.getLocalClassName() + ".provider",
                new File(uri.getPath()));
    }

}
