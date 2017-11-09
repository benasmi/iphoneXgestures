package com.mabe.productions.iphonexgestures;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class StartingActivity extends AwesomeSplash {

    private SharedPreferences sharedPreferences;

    @Override
    public void initSplash(ConfigSplash configSplash) {

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT > 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        configSplash.setBackgroundColor(R.color.splashBackground); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        configSplash.setLogoSplash(R.drawable.starting_icon); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: h

        configSplash.setTitleSplash("X-gestures");
        configSplash.setTitleTextColor(R.color.text_color);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.SlideInUp);
        configSplash.setTitleFont("fonts/openSans.ttf"); //provide string to your font located in assets
    }

    @Override
    public void animationsFinished() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("firstTime",true)){
                    sharedPreferences.edit().putBoolean("firstTime",false).commit();
                    Intent intent = new Intent(StartingActivity.this, IntroActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(StartingActivity.this, MainActivity.class);
                    startActivity(intent);
                }


            }
        }, 1000);

    }
}