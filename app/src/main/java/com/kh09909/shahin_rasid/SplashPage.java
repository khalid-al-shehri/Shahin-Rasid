package com.kh09909.shahin_rasid;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import maes.tech.intentanim.CustomIntent;

public class SplashPage extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);


        new Handler().postDelayed(new Runnable(){

            public void run(){
                Intent SplashIntent = new Intent(SplashPage.this, MainActivity.class);
                startActivity(SplashIntent);
                CustomIntent.customType(SplashPage.this, "fadein-to-fadeout");
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}