package com.gnine.galleryg2.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import com.gnine.galleryg2.callback.IBitmapCropCallback;
import com.gnine.galleryg2.model.CropParameters;
import com.gnine.galleryg2.model.ExifInfor;
import com.gnine.galleryg2.model.ImageState;
import com.gnine.galleryg2.util.FileUtils;
import com.gnine.galleryg2.util.ImageHeaderParser;

import java.io.File;
import java.io.IOException;

public class BitmapCropTask extends AsyncTask<Void,Void,Throwable> {

    public static final String TAG="BitmapCropTask";
    static{
        System.loadLibrary("crop");
    }

    private Bitmap viewBitmap;
    
    private final RectF cropRect;
    private final RectF currentImageRect;

    private float currentScale, currentAngle;
    private final int maxResultImageSizeX,maxResultImageSizeY;
    
    private final Bitmap.CompressFormat compressFormat;
    private final int compressQuality;
    private final String imageInputPath,imageOutputPath;
    private final ExifInfor exifInfor;
    private final IBitmapCropCallback iBitmapCropCallback;
    
    private int cropImageWidth,cropImageHeight;
    private int cropOffsetX,cropOffsetY;

    public BitmapCropTask(@Nullable Bitmap viewBitmap, @NonNull ImageState imageState,
                          @NonNull CropParameters cropParameters,@Nullable IBitmapCropCallback cropCallback){
        this.viewBitmap = viewBitmap;
        this.cropRect = imageState.getCropRect();
        this.currentImageRect = imageState.getCurrentImageRect();

        this.currentScale = imageState.getCurrentScale();
        this.currentAngle = imageState.getCurrentAngle();
        this.maxResultImageSizeX = cropParameters.getMaxResultImageSizeX();
        this.maxResultImageSizeY = cropParameters.getMaxResultImageSizeY();

        this.compressFormat = cropParameters.getCompressFormat();
        this.compressQuality = cropParameters.getCompressQuality();

        this.imageInputPath = cropParameters.getImageInputPath();
        this.imageOutputPath = cropParameters.getImageOutputPath();
        this.exifInfor = cropParameters.getExifInfor();

        this.iBitmapCropCallback = cropCallback;
    }

    @Override
    @Nullable
    protected Throwable doInBackground(Void... params) {
        if(viewBitmap==null){
            return new NullPointerException("ViewBitmap is null");
        }else if(viewBitmap.isRecycled()){
            return new NullPointerException("ViewBitmap is recycled");
        }else if(currentImageRect.isEmpty()){
            return new NullPointerException("CurrentImageRect is empty");
        }
        float resizeScale=resize();
        try{
            crop(resizeScale);
            viewBitmap=null;
        }catch (Throwable throwable){
            return throwable;
        }
        return null;
    }

    private float resize(){
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imageInputPath,options);
        boolean swapSides=exifInfor.getExifDegrees()==90 || exifInfor.getExifDegrees()==270;
        float scaleX=(swapSides?options.outHeight:options.outWidth)/(float)viewBitmap.getWidth();
        float scaleY=(swapSides?options.outWidth:options.outHeight)/(float)viewBitmap.getHeight();

        float resizeScale=Math.min(scaleX,scaleY);
        currentScale/=resizeScale;
        resizeScale=1;
        if(maxResultImageSizeX>0 && maxResultImageSizeY>0){
            float cropWidth=cropRect.width()/currentScale;
            float cropHeight=cropRect.height()/currentScale;
            if(cropWidth>maxResultImageSizeX ||cropHeight>maxResultImageSizeY){
                scaleX=maxResultImageSizeX/cropWidth;
                scaleY=maxResultImageSizeY/cropHeight;
                resizeScale=Math.min(scaleX,scaleY);
                currentScale/=resizeScale;
            }
        }
        return resizeScale;
    }

    private boolean crop(float resizeScale) throws IOException{
        ExifInterface originalExif=new ExifInterface(imageInputPath);
        cropOffsetX=Math.round((cropRect.left-currentImageRect.left)/currentScale);
        cropOffsetY=Math.round((cropRect.top-currentImageRect.top)/currentScale);
        cropImageWidth=Math.round(cropRect.width()/currentScale);
        cropImageHeight=Math.round(cropRect.height()/currentScale);

        boolean shouldCrop=shouldCrop(cropImageWidth,cropImageHeight);
        Log.i(TAG,"Should crop: "+shouldCrop);

        if(shouldCrop){
            boolean cropped = cropCImg(imageInputPath,imageOutputPath,
                    cropOffsetX,cropOffsetY,cropImageWidth,cropImageHeight,
                    currentAngle,resizeScale,compressFormat.ordinal(),compressQuality,
                    exifInfor.getExifDegrees(),exifInfor.getExifTranslation());
            if(cropped&&compressFormat.equals(Bitmap.CompressFormat.JPEG)){
                ImageHeaderParser.copyExif(originalExif,cropImageWidth,cropImageHeight,imageOutputPath);
            }
            return cropped;
        }else{
            FileUtils.copyFile(imageInputPath,imageOutputPath);
            return false;
        }
    }

    private boolean shouldCrop(int width,int height){
        int pixelError=1;
        pixelError+=Math.round(Math.max(width,height)/1000f);
        return (maxResultImageSizeX>0&&maxResultImageSizeY>0
        ||Math.abs(cropRect.left-currentImageRect.left)>pixelError
        ||Math.abs(cropRect.top-currentImageRect.top)>pixelError
        ||Math.abs(cropRect.bottom-currentImageRect.bottom)>pixelError
        ||Math.abs(cropRect.right-currentImageRect.right)>pixelError
        ||currentAngle!=0);
    }

    @SuppressWarnings("JniMissingFunction")
    native public static boolean cropCImg(String inputPath,String outputPath,
                                          int left,int top,int width,int height,
                                          float angle,float resizeScale,
                                          int format,int quality,
                                         int exifDegrees,int exifTranslation) throws IOException,OutOfMemoryError;

    @Override
    protected void onPostExecute(@Nullable Throwable throwable) {
        if(iBitmapCropCallback!=null){
            if(throwable==null){
                Uri uri=Uri.fromFile(new File(imageOutputPath));
                iBitmapCropCallback.onBitmapCropped(uri,cropOffsetX,cropOffsetY,cropImageWidth,cropImageHeight);
            }else{
                iBitmapCropCallback.onCropFailure(throwable);
            }
        }
    }
}
