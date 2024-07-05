package com.example.fuelbee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductDetail extends Fragment {

    public ProductDetail() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Navigate to FuelLocation fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new homeFragment())
                    .addToBackStack(null)  // Optional: add to back stack if you want to allow user to navigate back
                    .commit();
        });

        return view;
    }

    public static ProductDetail newInstance(int image, String title, String distance) {
        ProductDetail fragment = new ProductDetail();
        Bundle args = new Bundle();
        args.putInt("image", image);
        args.putString("title", title);
        args.putString("distance", distance);
        fragment.setArguments(args);
        return fragment;
    }
}
