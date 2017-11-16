package com.mabe.productions.iphonexgestures;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {
    public static MyAccessibilityService instance;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        Log.i("TEST", "onbind");
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("TEST", "unbind");
        instance = null;
        return super.onUnbind(intent);
    }
}
