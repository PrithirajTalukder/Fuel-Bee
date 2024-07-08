package com.example.fuelbee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
            // Navigate to homeFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new homeFragment())
                    .addToBackStack(null)  // Optional: add to back stack if you want to allow user to navigate back
                    .commit();
        });

        setupAddToCartFunctionality(view, R.id.add_to_cart_button_petrol, R.id.petrol_quantity_layout,
                R.id.decrement_button_petrol, R.id.increment_button_petrol, R.id.quantity_text_petrol);

        setupAddToCartFunctionality(view, R.id.add_to_cart_button_octane, R.id.octane_quantity_layout,
                R.id.decrement_button_octane, R.id.increment_button_octane, R.id.quantity_text_octane);

        setupAddToCartFunctionality(view, R.id.add_to_cart_button_diesel, R.id.diesel_quantity_layout,
                R.id.decrement_button_diesel, R.id.increment_button_diesel, R.id.quantity_text_diesel);

        Button proceedToCartButton = view.findViewById(R.id.proceed_to_cart_button);
        proceedToCartButton.setOnClickListener(v -> {

            Fragment fragment = new cartFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        return view;
    }

    private void setupAddToCartFunctionality(View view, int addToCartButtonId, int quantityLayoutId,
                                             int decrementButtonId, int incrementButtonId, int quantityTextId) {
        Button addToCartButton = view.findViewById(addToCartButtonId);
        LinearLayout quantityLayout = view.findViewById(quantityLayoutId);
        Button decrementButton = view.findViewById(decrementButtonId);
        Button incrementButton = view.findViewById(incrementButtonId);
        TextView quantityText = view.findViewById(quantityTextId);

        addToCartButton.setOnClickListener(v -> {
            addToCartButton.setVisibility(View.GONE);
            quantityLayout.setVisibility(View.VISIBLE);
        });

        decrementButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityText.getText().toString());
            if (quantity > 0) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        incrementButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantityText.getText().toString());
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });
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
