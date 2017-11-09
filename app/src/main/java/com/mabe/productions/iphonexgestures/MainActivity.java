package com.mabe.productions.iphonexgestures;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat service_switch;
    private RelativeLayout layout;
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



        layout = (RelativeLayout) findViewById(R.id.activity_main);

        service_switch = (SwitchCompat) findViewById(R.id.center_switch);
        service_switch.setChecked(sharedPreferences.getBoolean("switchState",true));

        if(service_switch.isChecked()){
            infoTxt.setText("ON");
            infoTxt.setTextColor(Color.parseColor("#FFFFFF"));
            OverlayShowingService.serviceIsWorking = true;
            svc = new Intent(MainActivity.this, OverlayShowingService.class);
            layout.setBackgroundColor(Color.parseColor("#ff4081"));
            startService(svc);
        }else{
            infoTxt.setText("OFF");
            infoTxt.setTextColor(Color.parseColor("#ff4081"));
            OverlayShowingService.serviceIsWorking = false;
        }

        if(sharedPreferences.getBoolean("firstTimeActivity",true)){
            infoTxt.setText("Gestures are on. Try it yourself!");
            infoTxt.setTextSize(25);
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
                    infoTxt.setTextColor(Color.parseColor("#FFFFFF"));
                    //Toast.makeText(MainActivity.this,"Service started...",Toast.LENGTH_LONG).show();
                    layout.setBackgroundColor(Color.parseColor("#ff4081"));
                    splashFromBottom();
                    OverlayShowingService.serviceIsWorking = true;

                }else{
                    OverlayShowingService.serviceIsWorking = false;
                    infoTxt.setText("OFF");
                    infoTxt.setTextColor(Color.parseColor("#ff4081"));
                    stopService(svc);
                    //Toast.makeText(MainActivity.this,"Service stopped...",Toast.LENGTH_LONG).show();
                    layout.setBackgroundColor(Color.parseColor("#ecf0f1"));
                    splashFromTop();


                }
            }
        });






}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void splashFromBottom(){
        int x = layout.getRight();
        int y = layout.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(layout.getWidth(), layout.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(layout, x, y, startRadius, endRadius);


        anim.setDuration(700);
        anim.start();



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void splashFromTop(){
        int x = layout.getLeft();
        int y = layout.getTop();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(layout.getWidth(), layout.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(layout, x, y, startRadius, endRadius);
        anim.setDuration(700);

        anim.start();
    }



}