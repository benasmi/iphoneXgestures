package com.mabe.productions.iphonexgestures;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.codetail.animation.SupportAnimator;
import io.codetail.widget.RevealFrameLayout;


public class MainActivity extends AppCompatActivity {

    private static final int SYSTEM_OVERLAY_PERMISSION = 1;
    private SwitchCompat service_switch;
    private RelativeLayout content_layout;
    private RevealFrameLayout root_layout;
    private Intent svc;
    private TextView infoTxt;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        infoTxt = (TextView) findViewById(R.id.info_txt);
        Typeface tfLight = Typeface.createFromAsset(getAssets(),
                "fonts/openSans.ttf");
        infoTxt.setTypeface(tfLight);

        content_layout = (RelativeLayout) findViewById(R.id.content_layout);
        root_layout = (RevealFrameLayout) findViewById(R.id.rootLayout);
        service_switch = (SwitchCompat) findViewById(R.id.center_switch);
        service_switch.setChecked(sharedPreferences.getBoolean("switchState",true));

        if(!CheckingUtils.isAccessibilityServiceEnabled(this, MyAccessibilityService.class)){
            CheckingUtils.createErrorBox("Please enable Accessibility Service in settings.", this, R.style.CasualStyle);
        }

        /*
         * Asking for system overlay permission.
         */
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW);
        if(android.os.Build.VERSION.SDK_INT >= 26 && permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    SYSTEM_OVERLAY_PERMISSION);
        }




        if(service_switch.isChecked()){
            infoTxt.setText("ON");
            infoTxt.setTextSize(35);
            root_layout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            infoTxt.setTextColor(Color.parseColor("#FFFFFF"));
            OverlayShowingService.serviceIsWorking = true;
            svc = new Intent(MainActivity.this, OverlayShowingService.class);
            content_layout.setBackgroundColor(Color.parseColor("#ff4081"));
            startService(svc);
        }else{
            infoTxt.setText("OFF");
            root_layout.setBackgroundColor(getResources().getColor(R.color.light));
            infoTxt.setTextColor(Color.parseColor("#ff4081"));
            OverlayShowingService.serviceIsWorking = false;
        }

        if(sharedPreferences.getBoolean("firstTimeActivity",true)){
            infoTxt.setText("Gestures are on. Try it yourself!");
            infoTxt.setTextSize(25);
            infoTxt.setTextSize(35);
            sharedPreferences.edit().putBoolean("firstTimeActivity",false).commit();
        }


        service_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("switchState", b).commit();
                //On
                if(b){
                    svc = new Intent(MainActivity.this, OverlayShowingService.class);
                    startService(svc);
                    infoTxt.setText("ON");
                    infoTxt.setTextSize(35);
                    infoTxt.setTextColor(Color.parseColor("#FFFFFF"));
                    //Toast.makeText(MainActivity.this,"Service started...",Toast.LENGTH_LONG).show();
                    splash(R.color.light, R.color.colorAccent);
                    OverlayShowingService.serviceIsWorking = true;

                }else{
                    OverlayShowingService.serviceIsWorking = false;
                    infoTxt.setText("OFF");
                    infoTxt.setTextSize(35);
                    infoTxt.setTextColor(Color.parseColor("#ff4081"));
                    stopService(svc);
                    //Toast.makeText(MainActivity.this,"Service stopped...",Toast.LENGTH_LONG).show();
                    splash(R.color.colorAccent, R.color.light);
                }
            }
        });

}


    private void splash(final int startColor, final int targetColor){
        root_layout.setBackgroundColor(getResources().getColor(startColor));
        content_layout.setBackgroundColor(getResources().getColor(targetColor));

        // get the center for the clipping circle
        int cx = (content_layout.getLeft() + content_layout.getRight()) / 2;
        int cy = (content_layout.getTop() + content_layout.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, content_layout.getWidth() - cx);
        int dy = Math.max(cy, content_layout.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        SupportAnimator animator =
                io.codetail.animation.ViewAnimationUtils.createCircularReveal(content_layout, cx, cy, 0, finalRadius);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500);
        animator.start();



    }




}