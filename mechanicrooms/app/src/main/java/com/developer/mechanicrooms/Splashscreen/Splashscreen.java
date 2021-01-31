package com.developer.mechanicrooms.Splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.developer.mechanicrooms.Addlocation;
import com.developer.mechanicrooms.RegPageActivity;
import com.developer.mechanicrooms.R;
import com.developer.mechanicrooms.Utils.MyActivity;

public class Splashscreen extends MyActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3500;
    ImageView imageView;
Boolean chksession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
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
                    Intent intent=new Intent(getApplicationContext(), Addlocation.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    finish();
                    Intent i = new Intent(Splashscreen.this, RegPageActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
}

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent mainIntent = new Intent(Splashscreen.this, MainActivity.class);
//                Splashscreen.this.startActivity(mainIntent);
//                Splashscreen.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);
//    }
//}
