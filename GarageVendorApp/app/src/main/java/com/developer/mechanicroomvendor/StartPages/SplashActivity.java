package com.developer.mechanicroomvendor.StartPages;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.developer.mechanicroomvendor.HomeActivity;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Utils.MyActivity;

public class SplashActivity extends MyActivity {
    private static  int splashtime = 9000;
    ImageView imageView;
    boolean chksession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        chksession=pref.getBoolean("logged_in", Boolean.parseBoolean(""));

        imageView = findViewById(R.id.imagev);
        final Animation zoomAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom);
        imageView.startAnimation(zoomAnimation);
        zoomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (chksession==true)
                {
                    Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    finish();
                    Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        },splashtime);
    }
}