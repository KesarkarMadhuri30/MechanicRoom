package com.developer.mechanicrooms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageSwitcher;

import com.developer.mechanicrooms.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.Timer;

public class Mainpage extends Activity {


    private int position = 0;

    private Timer timer = null;
    int gallery[] =
            {
//                    R.drawable.car_icon,
//                    R.drawable.circle_cropped,
//                    R.drawable.crclimage,
//                    R.drawable.loginbg,
//                    R.drawable.car_icon,
//                    R.drawable.circle_cropped,
//                    R.drawable.crclimage,
//                    R.drawable.loginbg,
            };

    ImageSwitcher imgSwitcher;

    GridView simpleList;
    ArrayList birdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

//        imgSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);

        simpleList = (GridView) findViewById(R.id.simpleGridView);
//        birdList.add(new Item("Bird services", R.drawable.service1));
//        birdList.add(new Item("Bird car", R.drawable.service2));
//        birdList.add(new Item("Bird bike", R.drawable.spalas));
//        birdList.add(new Item("Bird services", R.drawable.service1));
//        birdList.add(new Item("Bird car", R.drawable.service2));
//        birdList.add(new Item("Bird bike", R.drawable.spalas));

        MyAdapter myAdapter = new MyAdapter(this, R.layout.grid_view_items, birdList);
        simpleList.setAdapter(myAdapter);
    }
}

//        imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//
//            public View makeView() {
//                return new ImageView(getApplicationContext());
//            }
//        });
//        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
//        imgSwitcher.setInAnimation(fadeIn);
//        imgSwitcher.setOutAnimation(fadeOut);
//    }
//
//    public void start(View button) {
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            public void run() {
//                // avoid exception: "Only the original thread that created a view hierarchy can touch its views"
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        imgSwitcher.setImageResource(gallery[position]);
//                        position++;
//                        if (position == 6) {
//                            position = 0;
//                        }
//                    }
//                });
//            }
//
//        }, 0, 2500);
//
//    }
//
//    public void stop(View button) {
//        timer.cancel();
//    }
//}
