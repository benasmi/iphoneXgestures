package com.mabe.productions.iphonexgestures;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {
    public static MyAccessibilityService instance;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
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
