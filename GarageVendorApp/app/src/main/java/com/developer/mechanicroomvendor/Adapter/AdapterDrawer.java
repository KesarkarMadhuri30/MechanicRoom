package com.developer.mechanicroomvendor.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.mechanicroomvendor.R;

public class AdapterDrawer extends ArrayAdapter<String>
{
    private final Activity context;
    String[] menus;
    int[] images = {
            R.drawable.account,
            R.drawable.ic_baseline_rate_review_24,
            R.drawable.aboutus,
            R.drawable.ic_baseline_policy_24,
            R.drawable.contact_us,
            R.drawable.share,
            R.drawable.logout
    };



    public AdapterDrawer(Activity context, String[] menus)
    {
        super(context, R.layout.list_social_items,menus);
        this.context = context;
        this.menus = menus;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {

        LayoutInflater inflator = context.getLayoutInflater();
        View rowView = inflator.inflate(R.layout.list_social_items, null,true);

        TextView txtv_menu;
        ImageView drwimg;
        txtv_menu = (TextView)rowView.findViewById(R.id.txtv_menu);
        txtv_menu.setText(""+menus[position]);

        drwimg = (ImageView)rowView.findViewById(R.id.drwimg);
        drwimg.setImageResource(Integer.parseInt(""+images[position]));

        return rowView;
        // }
    }

}



