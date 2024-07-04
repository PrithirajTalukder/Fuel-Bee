package com.example.fuelbee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {

    private List<CardItem> imageList;
    private RecyclerView homeRecyclerView;
    private CardAdapter cardAdapter;

    public homeFragment(){

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        homeRecyclerView = rootView.findViewById(R.id.recyclerHome);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        imageList = generateCardItems();
        cardAdapter = new CardAdapter(imageList);
        homeRecyclerView.setAdapter(cardAdapter);

        return rootView;
    }
    private List<CardItem> generateCardItems(){
        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem(R.drawable.petrol1, "Petrol Pump 1", "500m away"));
        cardItems.add(new CardItem(R.drawable.petrol1, "Petrol Pump 2", "300m away"));
        cardItems.add(new CardItem(R.drawable.petrol1, "Petrol Pump 3", "600m away"));
        cardItems.add(new CardItem(R.drawable.petrol1, "Petrol Pump 4", "800m away"));

        return cardItems;

    }
}