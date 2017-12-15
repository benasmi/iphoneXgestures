package com.mabe.productions.iphonexgestures;

/**
 * Created by Benas on 11/5/2017.
 */

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OverlayShowingService extends Service{


    private static int SWIPE_MIN_DISTANCE;
    public static boolean serviceIsWorking = true;
    private boolean firstTime = false;
    private boolean stillTouched = false;
    private ImageView animationImageView;

    long startingTime = 0;
    double startingY = 0;

    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("TEST", "overlay showing service started");

        SWIPE_MIN_DISTANCE = (int) CheckingUtils.convertPixelsToDp(120, getApplicationContext());

        serviceIsWorking = true;
        //Center ImageView | SUBVIEW
        animationImageView = new ImageView(this);
            animationImageView.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams animationImgLayout = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(120,this),(int)CheckingUtils.convertPixelsToDp(120,this));
        animationImageView.setLayoutParams(animationImgLayout);

        //ImageView | SUBVIEW
        ImageView bottomImage = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)CheckingUtils.convertPixelsToDp(200,this),(int)CheckingUtils.convertPixelsToDp(15,this));
        bottomImage.setLayoutParams(layoutParams);
        bottomImage.setBackgroundColor(Color.TRANSPARENT);
        bottomImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {




                if (event.getAction() == MotionEvent.ACTION_UP) {

                    stillTouched = false;
                    Log.i("TEST", "PAKEITE BOOLENA į false");
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    stillTouched = true;
                    firstTime = true;
                    startingTime = System.currentTimeMillis();
                    startingY = event.getY();
                    Log.i("TEST", "PAKEITE BOOLENA į true");
                }

                long passedTime = System.currentTimeMillis() - startingTime;


                if(/*passedTime<800 && */firstTime && serviceIsWorking){

                    Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getY() - startingY) > SWIPE_MIN_DISTANCE){
                        firstTime = !firstTime;
                        new Handler().postDelayed(new Runnable() {
                     @Override
                        public void run() {
                        if(stillTouched){
                            try{
                                if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                    recentAppsRiseFade();
                                    MyAccessibilityService.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please enable Accessibility services for X-Gestures!", Toast.LENGTH_LONG).show();
                                }

                            }catch (Exception e){
                            }
                        }else{

                            if(CheckingUtils.isAccessibilityServiceEnabled(OverlayShowingService.this, MyAccessibilityService.class)){
                                homeRiseFade();
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

        //Center Layout | SUPERVIEW
        final LinearLayout centerLayout = new LinearLayout(this);
        centerLayout.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(200,this),(int)CheckingUtils.convertPixelsToDp(25,this)));
        centerLayout.addView(animationImageView);

        //BOTTOM WINDOW MANAGER PARAMS
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER;


        //CENTER WINDOW MANAGER PARAMS
        final WindowManager.LayoutParams centerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT >= 26 ? LayoutParams.TYPE_APPLICATION_OVERLAY : LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        centerParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;


        //WINDOW MANAGER
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(layoutBottom, params);

        new Handler().postDelayed(new Runnable() {
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


    private void recentAppsRiseFade(){
        animationImageView.setImageResource(R.drawable.recentapps);

        Animation homeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.risefade);
        animationImageView.startAnimation(homeAnimation);
    }



}

