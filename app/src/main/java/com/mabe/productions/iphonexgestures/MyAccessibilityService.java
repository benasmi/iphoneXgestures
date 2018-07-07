package com.mabe.productions.iphonexgestures;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {
    public static MyAccessibilityService instance;
    public static String currentApp = "1";
    public static String lastApp = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            try {
                ApplicationInfo app = MyAccessibilityService.this.getPackageManager().getApplicationInfo(String.valueOf(event.getPackageName()), 0);
                Log.i("TEST", String.valueOf(CheckingUtils.isSYSTEM(app)));

                if (event.getPackageName() != null && event.getClassName() != null &&
                        !currentApp.equals(event.getPackageName())) {

                    //Checking if it's possible to create intent;
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(String.valueOf(event.getPackageName()));
                    if(launchIntent==null) {
                        return;
                    }

                    if(!lastApp.equals(currentApp)){
                        lastApp = currentApp;


                    }



                    currentApp = String.valueOf(event.getPackageName());




                    Log.i("TEST", "Current application: " + currentApp);
                    Log.i("TEST", "Last application: " + lastApp);
                    Log.i("TEST", "--------------------------------------");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }



        }
    }

    @Override
    public void onInterrupt() {
        Log.i("TEST", "onInterrupt");
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Log.i("TEST", "bindService");
        return super.bindService(service, conn, flags);
    }

    @Nullable
    public static MyAccessibilityService getInstance(){
        return instance;
    }

    @Override
    protected void onServiceConnected() {
        Log.i("TEST", "Accessibility service connected.");
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("TEST", "Accessibility service unbinded.");
        instance = null;
        return super.onUnbind(intent);
    }
}
