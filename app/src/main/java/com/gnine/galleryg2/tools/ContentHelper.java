package com.gnine.galleryg2.tools;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ContentHelper {
    public static boolean moveFile(String sourcePath, String targetPath) {
        File from = new File(sourcePath);
        File to = new File(targetPath);
        File target = new File(to, from.getName());

        boolean succ = false;
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(target).getChannel();
            inputChannel = new FileInputStream(from).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            outputChannel.close();
            from.delete();
            succ = true;
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return succ;
    }
}
