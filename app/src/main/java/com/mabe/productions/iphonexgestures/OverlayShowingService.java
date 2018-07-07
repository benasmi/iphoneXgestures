package com.mabe.productions.iphonexgestures;

/**
 * Created by Benas on 11/5/2017.
 */

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverlayShowingService extends Service{


    private static int BOTTOM_SWIPE_MIN_DISTANCE;
    private static int BOTTOM_LEFT_SWIPE_MIN_DISTANCE;
    private static int CENTER_RIGHT_MIN_DISTANCE;

    public static boolean serviceIsWorking = true;
    private boolean firstTime = false;
    private boolean stillTouched = false;
    private ImageView animationImageView;



    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

       // Log.i("TEST", "overlay showing service started");

        BOTTOM_SWIPE_MIN_DISTANCE = (int) CheckingUtils.convertPixelsToDp(120, getApplicationContext());
        BOTTOM_LEFT_SWIPE_MIN_DISTANCE = (int) CheckingUtils.convertPixelsToDp(20, getApplicationContext());
        CENTER_RIGHT_MIN_DISTANCE = (int) CheckingUtils.convertPixelsToDp(20, getApplicationContext());

        serviceIsWorking = true;
        //Center ImageView | SUBVIEW
        animationImageView = new ImageView(this);
            animationImageView.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams animationImgLayout = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(120,this),(int)CheckingUtils.convertPixelsToDp(120,this));
        animationImageView.setLayoutParams(animationImgLayout);

        //Left Bottom ImageView | SUBVIEW
        ImageView leftBottomImage = new ImageView(this);
        LinearLayout.LayoutParams leftBottomImageParams = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(30,this),(int)CheckingUtils.convertPixelsToDp(30,this));
        leftBottomImage.setLayoutParams(leftBottomImageParams);
        leftBottomImage.setBackgroundColor(Color.TRANSPARENT);
        leftBottomImage.setOnTouchListener(new OnTouchListener() {

            long startingTime = 0;
            double startingX = 0;



            @Override
            public boolean onTouch(View view, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    stillTouched = false;
                    // Log.i("TEST", "PAKEITE BOOLENA į false");
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    stillTouched = true;
                    firstTime = true;
                    startingTime = System.currentTimeMillis();
                    startingX = event.getX();
                    //Log.i("TEST", "PAKEITE BOOLENA į true");
                }

                long passedTime = System.currentTimeMillis() - startingTime;

                if(/*passedTime<800 && */firstTime && serviceIsWorking){

                    //Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getX() - startingX) > BOTTOM_SWIPE_MIN_DISTANCE){
                        firstTime = !firstTime;




                        try{
                            switchApplications();
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(MyAccessibilityService.lastApp);
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchIntent);

                        }catch (Exception e){
                            Log.i("TEST", "First time");
                        }



                        try{
                            switchApplications();
                            if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                switchApplications();
                                if(MyAccessibilityService.instance == null){
                                    Toast.makeText(OverlayShowingService.this, "Please reboot your phone to start AccessibilityService.", Toast.LENGTH_LONG);
                                    return true;
                                }


                            }else{
                                Toast.makeText(getApplicationContext(), "Please enable Accessibility services for X-Gestures!", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                        }

                    }
                }
                return true;

            }
        });

        //Right Center ImageView | SUBVIEW
        ImageView rightCenter = new ImageView(this);
        LinearLayout.LayoutParams rightCenterLayout = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(15,this),(int)CheckingUtils.convertPixelsToDp(70,this));
        rightCenter.setLayoutParams(rightCenterLayout);
        rightCenter.setBackgroundColor(Color.TRANSPARENT);
        rightCenter.setOnTouchListener(new OnTouchListener() {

            long startingTime = 0;
            double startingX = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    stillTouched = false;
                    // Log.i("TEST", "PAKEITE BOOLENA į false");
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    stillTouched = true;
                    firstTime = true;
                    startingTime = System.currentTimeMillis();
                    startingX = event.getX();
                    //Log.i("TEST", "PAKEITE BOOLENA į true");
                }

                long passedTime = System.currentTimeMillis() - startingTime;

                if(/*passedTime<800 && */firstTime && serviceIsWorking){

                    //Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getX() - startingX) > CENTER_RIGHT_MIN_DISTANCE){
                        firstTime = !firstTime;




                        try{
                            MyAccessibilityService.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

                        }catch (Exception e){
                            Log.i("TEST", "First time");
                        }



                        try{

                            if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                backFade();
                                if(MyAccessibilityService.instance == null){
                                    Toast.makeText(OverlayShowingService.this, "Please reboot your phone to start AccessibilityService.", Toast.LENGTH_LONG);
                                    return true;
                                }


                            }else{
                                Toast.makeText(getApplicationContext(), "Please enable Accessibility services for X-Gestures!", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                        }

                    }
                }
                return true;
            }
        });


        //Right Top ImageView | SUBVIEW
        ImageView topRight = new ImageView(this);
        LinearLayout.LayoutParams topRightLayout = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(40,this),(int)CheckingUtils.convertPixelsToDp(40,this));
        topRight.setLayoutParams(rightCenterLayout);
        topRight.setBackgroundColor(Color.RED);
        topRight.setOnTouchListener(new OnTouchListener() {

            long startingTime = 0;
            double startingX = 0;
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    stillTouched = false;
                    // Log.i("TEST", "PAKEITE BOOLENA į false");
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    stillTouched = true;
                    firstTime = true;
                    startingTime = System.currentTimeMillis();
                    startingX = event.getX();
                    //Log.i("TEST", "PAKEITE BOOLENA į true");
                }

                long passedTime = System.currentTimeMillis() - startingTime;

                if(/*passedTime<800 && */firstTime && serviceIsWorking){

                    //Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getX() - startingX) > CENTER_RIGHT_MIN_DISTANCE){
                        firstTime = !firstTime;




                        try{
                            CheckingUtils.takeScreenshot((Activity)OverlayShowingService.this.getApplicationContext());
                        }catch (Exception e){
                            Log.i("TEST", "First time");
                        }



                        try{

                            if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                backFade();
                                if(MyAccessibilityService.instance == null){
                                    Toast.makeText(OverlayShowingService.this, "Please reboot your phone to start AccessibilityService.", Toast.LENGTH_LONG);
                                    return true;
                                }


                            }else{
                                Toast.makeText(getApplicationContext(), "Please enable Accessibility services for X-Gestures!", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                        }

                    }
                }
                return true;
            }
        });


        //Bottom ImageView | SUBVIEW
        ImageView bottomImage = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(200,this),(int)CheckingUtils.convertPixelsToDp(15,this));
        bottomImage.setLayoutParams(layoutParams);
        bottomImage.setBackgroundColor(Color.TRANSPARENT);
        bottomImage.setOnTouchListener(new View.OnTouchListener() {

            long startingTime = 0;
            double startingY = 0;

            @Override
            public boolean onTouch(final View view, final MotionEvent event) {




                if (event.getAction() == MotionEvent.ACTION_UP) {

                    stillTouched = false;
                   // Log.i("TEST", "PAKEITE BOOLENA į false");
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    stillTouched = true;
                    firstTime = true;
                    startingTime = System.currentTimeMillis();
                    startingY = event.getY();
                    //Log.i("TEST", "PAKEITE BOOLENA į true");
                }

                long passedTime = System.currentTimeMillis() - startingTime;


                if(/*passedTime<800 && */firstTime && serviceIsWorking){

                    //Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getY() - startingY) > BOTTOM_SWIPE_MIN_DISTANCE){
                        firstTime = !firstTime;
                        new Handler().postDelayed(new Runnable() {
                     @Override
                        public void run() {
                        if(stillTouched){
                            try{

                                if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                    recentAppsRiseFade();
                                    if(MyAccessibilityService.instance == null){
                                        Toast.makeText(OverlayShowingService.this, "Please reboot your phone to start AccessibilityService.", Toast.LENGTH_LONG);
                                        return;
                                    }
                                    MyAccessibilityService.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);

                                }else{
                                    Toast.makeText(getApplicationContext(), "Please enable Accessibility services for X-Gestures!", Toast.LENGTH_LONG).show();
                                }

                            }catch (Exception e){
                            }
                        }else{

                            if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                homeRiseFade();
                                if(MyAccessibilityService.instance == null){
                                   Toast.makeText(OverlayShowingService.this, "Please reboot your phone to start AccessibilityService.", Toast.LENGTH_LONG);
                                   return;
                                }
                                MyAccessibilityService.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                            }else{
                                Toast.makeText(getApplicationContext(), "Please enable Accessibility services for X-Gestures!", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                }, 350);

                    }
                }
                return true;
            }
        });


        //Bottom Layout | SUPERVIEW
        LinearLayout layoutBottom = new LinearLayout(this);
        layoutBottom.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(120,this),(int)CheckingUtils.convertPixelsToDp(120,this)));
        layoutBottom.addView(bottomImage);

        //Top Layout | SUPERVIEW
        LinearLayout rightLayoutTop = new LinearLayout(this);
        rightLayoutTop.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(120,this),(int)CheckingUtils.convertPixelsToDp(120,this)));
        rightLayoutTop.addView(topRight);


        //Left Bottom Layout | SUPERVIEW
        LinearLayout leftLayoutBottom = new LinearLayout(this);
        leftLayoutBottom.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(120,this),(int)CheckingUtils.convertPixelsToDp(120,this)));
        leftLayoutBottom.addView(leftBottomImage);

        //Center Right Layout | SUPERVIEW
        final LinearLayout centerLayoutRight = new LinearLayout(this);
        centerLayoutRight.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(120,this),(int)CheckingUtils.convertPixelsToDp(120,this)));
        centerLayoutRight.addView(rightCenter);

        //Center Layout | SUPERVIEW
        final LinearLayout centerLayout = new LinearLayout(this);
        centerLayout.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(200,this),(int)CheckingUtils.convertPixelsToDp(25,this)));
        centerLayout.addView(animationImageView);


        //BOTTOM WINDOW MANAGER PARAMS
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_PHONE, //TYPE_PHONE may need to be replaced with TYPE_TOAST
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER;


        //LEFT BOTTOM WINDOW MANAGER PARAMS
        WindowManager.LayoutParams leftBottomParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_PHONE, //TYPE_PHONE may need to be replaced with TYPE_TOAST
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        leftBottomParams.gravity = Gravity.BOTTOM | Gravity.LEFT;


        //CENTER RIGHT MANAGER PARAMS
        final WindowManager.LayoutParams centerRightParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_PHONE, //TYPE_PHONE may need to be replaced with TYPE_TOAST
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        centerRightParams.gravity = Gravity.CENTER | Gravity.RIGHT;

        //TOP RIGHT MANAGER PARAMS
        final WindowManager.LayoutParams topRightParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_PHONE, //TYPE_PHONE may need to be replaced with TYPE_TOAST
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        topRightParams.gravity = Gravity.TOP | Gravity.RIGHT;

        //CENTER WINDOW MANAGER PARAMS
        final WindowManager.LayoutParams centerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_PHONE, //TYPE_PHONE may need to be replaced with TYPE_TOAST
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        centerParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;


        //WINDOW MANAGER

        //Checking if SYSTEM_OVERLAY permission is granted, if api level is above or equal to 26
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED && android.os.Build.VERSION.SDK_INT >= 26){
            Toast.makeText(this, "Please enable SYSTEM_OVERLAY permission for X-Gestures in Settings!", Toast.LENGTH_LONG);
            return;
        }

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(layoutBottom, params);
        wm.addView(leftLayoutBottom,leftBottomParams);
        wm.addView(centerLayoutRight,centerRightParams);
        wm.addView(rightLayoutTop,topRightParams);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               wm.addView(centerLayout, centerParams);


            }
        }, 200l);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    private void homeRiseFade(){
        animationImageView.setImageResource(R.drawable.home);
        Animation homeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.risefade);
        animationImageView.startAnimation(homeAnimation);
    }

    private void switchApplications(){
        animationImageView.setImageResource(R.drawable.ic_switch_apps);
        Animation homeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.risefade);
        animationImageView.startAnimation(homeAnimation);
    }

    private void recentAppsRiseFade(){
        animationImageView.setImageResource(R.drawable.recentapps);

        Animation homeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.risefade);
        animationImageView.startAnimation(homeAnimation);
    }

    private void backFade(){
        animationImageView.setImageResource(R.drawable.ic_back_arrow);

        Animation homeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.risefade);
        animationImageView.startAnimation(homeAnimation);
    }





}

