package com.mabe.productions.iphonexgestures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Benas on 7/9/2018.
 */

public class ScreenshotManager {
    /*
    private static final String SCREENCAP_NAME = "screencap";


    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    public static final ScreenshotManager INSTANCE = new ScreenshotManager();
    private Intent mIntent;

    private ScreenshotManager() {
    }

    public void requestScreenshotPermission(@NonNull Activity activity, int requestId) {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        activity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), requestId);
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null){
            mIntent = data;
            OverlayShowingService.permisionIsGranted = true;
        }else{
            OverlayShowingService.permisionIsGranted = false;
            mIntent = null;
        }

        Log.i("TEST", "Permision status: " + OverlayShowingService.permisionIsGranted);



    }

    @UiThread
    public boolean takeScreenshot(@NonNull Context context) {
        if(!OverlayShowingService.permisionIsGranted) {
            Log.i("TEST", "Permission was denied: No media projector");
            return false;
        }else{
            Log.i("TEST", "Permission was given: Media projector has been created");
        }

        if (mIntent == null)
            return false;
            Log.i("TEST", "Nuotrauka saugojama");

        final MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        final MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mIntent);

        if (mediaProjection == null)
            return false;
        final int density = context.getResources().getDisplayMetrics().densityDpi;
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final Point windowSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealSize(windowSize);
        final int width = windowSize.x;
        final int height = windowSize.y;


        // start capture reader
        final ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        final VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay(SCREENCAP_NAME, width, height, density, VIRTUAL_DISPLAY_FLAGS, imageReader.getSurface(), null, null);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(final ImageReader reader) {
                Log.d("TEST", "onImageAvailable");
                mediaProjection.stop();
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(final Void... params) {
                        Image image = null;
                        Bitmap bitmap = null;
                        try {
                            image = reader.acquireLatestImage();
                            if (image != null) {
                                Image.Plane[] planes = image.getPlanes();
                                ByteBuffer buffer = planes[0].getBuffer();
                                int pixelStride = planes[0].getPixelStride(), rowStride = planes[0].getRowStride(), rowPadding = rowStride - pixelStride * width;
                                bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);
                                // fix the extra width from Image
                                Bitmap croppedBitmap;
                                try {
                                    croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, windowSize.x, windowSize.y);
                                } catch (OutOfMemoryError e) {
                                    Log.e("TEST", "Out of memory when cropping bitmap of screen size");
                                    croppedBitmap = bitmap;
                                }
                                if (croppedBitmap != bitmap) {
                                    bitmap.recycle();
                                }
                                saveImage(croppedBitmap,"Photo" + System.nanoTime());

                                return croppedBitmap;
                            }
                        } catch (Exception e) {
                            if (bitmap != null)
                                bitmap.recycle();
                            e.printStackTrace();
                        }
                        if (image != null)
                            image.close();
                        reader.close();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        Log.d("TEST", "Got bitmap?" + (bitmap != null));
                    }
                }.execute();
            }
        }, null);

        mediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                if (virtualDisplay != null)
                    virtualDisplay.release();
                imageReader.setOnImageAvailableListener(null, null);
                mediaProjection.unregisterCallback(this);
            }
        }, null);


        return true;
    }
    */
}