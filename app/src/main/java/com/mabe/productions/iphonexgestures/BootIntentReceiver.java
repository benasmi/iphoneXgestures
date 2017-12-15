package com.mabe.productions.iphonexgestures;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootIntentReceiver  extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceIntent = new Intent(context, OverlayShowingService.class);
            context.startService(serviceIntent);
        }
    }
}
