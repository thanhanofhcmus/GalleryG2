package com.gnine.galleryg2.callback;

import com.gnine.galleryg2.fragments.UCropFragment;

public interface UCropFragmentCallback {

    void loadingProgress(boolean showLoader);

    void onCropFinish(UCropFragment.UCropResult result);

}
