package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


//Creating splash screen to display company's logo

public class activity_splash extends AppCompatActivity {

    // Setting the time of splash
    private static int splashTimeOut = 2000;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //Referencing  the logo
        logo = findViewById(R.id.launcher_logo);

        //Automatically launching new activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(activity_splash.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);

        //Loading animation from animation folder.
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
        logo.startAnimation(myanim);

    }
}
