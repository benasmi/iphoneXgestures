package com.mabe.productions.iphonexgestures;

/**
 * Created by Benas on 11/5/2017.
 */

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Handler;
import android.os.IBinder;
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


    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
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


                if(passedTime<400 && firstTime){

                    Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getY() - startingY) > SWIPE_MIN_DISTANCE){
                        firstTime = !firstTime;
                        new Handler().postDelayed(new Runnable() {
                     @Override
                        public void run() {
                        if(stillTouched){
                            try{
                                recentAppsRiseFade();
                                Class serviceManagerClass = Class.forName("android.os.ServiceManager");
                                Method getService = serviceManagerClass.getMethod("getService", String.class);
                                IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
                                Class statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());
                                Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null, new Object[] { retbinder });
                                Method clearAll = statusBarClass.getMethod("toggleRecentApps");
                                clearAll.setAccessible(true);
                                clearAll.invoke(statusBarObject);
                            }catch (Exception e){

                            }


                        }else{
                            Log.i("TEST", "GoHomeScreen");
                            homeRiseFade();
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                        }

                    }
                }, 200);

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
        LinearLayout centerLayout = new LinearLayout(this);
        centerLayout.setLayoutParams(new ActionBar.LayoutParams((int)CheckingUtils.convertPixelsToDp(200,this),(int)CheckingUtils.convertPixelsToDp(25,this)));
        centerLayout.addView(animationImageView);

        //BOTTOM WINDOW MANAGER PARAMS
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER;


        //CENTER WINDOW MANAGER PARAMS
        WindowManager.LayoutParams centerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        centerParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;


        //WINDOW MANAGER
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(layoutBottom, params);
        wm.addView(centerLayout, centerParams);









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

