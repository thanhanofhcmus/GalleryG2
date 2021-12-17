package com.gnine.galleryg2.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrashData {
    public final String trashPath;
    public final String oldPath;
    public static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
    public final String dateDeleted;
    private boolean checked;

    public TrashData(String trashPath, String oldPath, String dateDeleted) {
        this.trashPath = trashPath;
        this.oldPath = oldPath;
        this.dateDeleted = dateDeleted;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
