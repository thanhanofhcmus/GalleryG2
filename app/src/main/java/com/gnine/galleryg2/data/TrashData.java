package com.gnine.galleryg2.data;

public class TrashData {
    public final String trashPath;
    public final String oldPath;
    public final long dateDeleted;
    private boolean checked;

    public TrashData(String trashPath, String oldPath, long dateDeleted) {
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
