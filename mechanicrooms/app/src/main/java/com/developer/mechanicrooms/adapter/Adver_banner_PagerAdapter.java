package com.developer.mechanicrooms.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.mechanicrooms.Model.BannerModel;
import com.developer.mechanicrooms.R;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

public class Adver_banner_PagerAdapter extends PagerAdapter {
    Context context;
    //int images[];
    private ArrayList<BannerModel> al_imgpaths;
    LayoutInflater layoutInflater;
    Dialog dialog_menus;

    public Adver_banner_PagerAdapter(Context context, ArrayList<BannerModel> al_imgpaths) {
        this.context = context;
        this.al_imgpaths = al_imgpaths;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return al_imgpaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.view_pager_imageview, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        //TextView label = (TextView) itemView.findViewById(R.id.txtv_banner_title);
        //label.setText(" "+al_v_name.get(position));
        //imageView.setImageResource(images[position]);
//        Picasso.with(context).load(al_imgpaths.get(position).getImage()).placeholder(R.drawable.applogo)
//                .error(R.drawable.applogo).into(imageView);

        Glide.with(context).load(al_imgpaths.get(position).getImage()).placeholder(R.drawable.mcroomlogo).error(R.drawable.mcroomlogo).into(imageView);

        container.addView(itemView);
        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if ((al_imgpaths != null && !al_imgpaths.isEmpty()))
                {
                   // String imageurl= String.valueOf(al_imgpaths.get(position).getImage());
                   // Toast.makeText(context, ""+imageurl, Toast.LENGTH_SHORT).show();
                    setuppopup(al_imgpaths.get(position).getImage());
                }
                else
                {
                    Toast.makeText(context, "Fetching values wait....", Toast.LENGTH_SHORT).show();
                }
            }

        });


        return itemView;
    }



    private void setuppopup(int imageurl1)
    {
        //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
        // String vid= al_v_id.get(position);

        //Toast.makeText(context, ""+imageurl1, Toast.LENGTH_SHORT).show();
        // String vname = al_v_name.get(position);
        //Intent i = new Intent(v.getContext(), Details.class);
        //i.putExtra("VendorID",vid);
        // i.putExtra("Name",vname);
        //i.putExtra("ImageUrl",imageurl);
        // v.getContext().startActivity(i);

        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View show_menu_popup = inflator.inflate(R.layout.popup_image, null);
        ImageView popimg1 = (ImageView)show_menu_popup.findViewById(R.id.popimg);
        //  Picasso.with(context).load(imageurl1).placeholder(R.drawable.applogo).error(R.drawable.applogo).into(popimg1);
        Glide.with(context).load(imageurl1).placeholder(R.drawable.mcroomlogo).error(R.drawable.mcroomlogo).into(popimg1);

        dialog_menus = new Dialog(context);
        dialog_menus.setContentView(show_menu_popup);
        dialog_menus.setTitle(" ");
        dialog_menus.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_menus.show();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}