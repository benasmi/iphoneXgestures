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

    long startingTime = 0;
    double startingY = 0;

    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final GestureDetector gdt = new GestureDetector(new GestureListener());

        //ImageView | SUBVIEW
        ImageView bottomImage = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,40);
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

//                Log.i("TEST", "Passed time: " + passedTime);
//                Log.i("TEST", "Swiped up: " + Math.abs((event.getY() - startingY)));

                if(passedTime<400 && firstTime){

                    Log.i("TEST", "Bottom to top");
                    if(Math.abs(event.getY() - startingY) > SWIPE_MIN_DISTANCE){
                        firstTime = !firstTime;
                        new Handler().postDelayed(new Runnable() {
                     @Override
                        public void run() {
                        if(stillTouched){
                            try{
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
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                        }

                    }
                }, 80);

                    }
                }


                gdt.onTouchEvent(event);
                return true;
            }
        });

        //Layout | SUPERVIEW
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new ActionBar.LayoutParams(500,40));
        layout.addView(bottomImage);



        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);




        wm.addView(layout, params);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Log.i("TEST", "Right to left");
//                return false; // Right to left
//
//            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Log.i("TEST", "Left to right");
//                return false; // Left to right
//            }
//
//            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                //Log.i("TEST", "Bottom to top");
////
////
//
//
//
//
//                return false; // Bottom to top
//            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                Log.i("TEST", "Top to bottom");
//                return false; // Top to bottom
//            }
            return false;
        }

    }

    private void toggleRecents() {

        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
        closeRecents.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
        closeRecents.setComponent(recents);
        this.startActivity(closeRecents);
    }
}

