package com.developer.mechanicrooms.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.developer.mechanicrooms.R;
import com.developer.mechanicrooms.adapter.MyAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ArrayList birdList = new ArrayList<>();

    GridView simpleList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.activity_mainpage, container, false);

        simpleList = (GridView) root.findViewById(R.id.simpleGridView);
//        birdList.add(new Item("Bird services", R.drawable.service1));
//        birdList.add(new Item("Bird car", R.drawable.service2));
//        birdList.add(new Item("Bird bike", R.drawable.spalas));
//        birdList.add(new Item("Bird services", R.drawable.service1));
//        birdList.add(new Item("Bird car", R.drawable.service2));
//        birdList.add(new Item("Bird bike", R.drawable.spalas));

        MyAdapter myAdapter = new MyAdapter(getContext(), R.layout.grid_view_items, birdList);
        simpleList.setAdapter(myAdapter);

        return root;
    }
}