package com.example.fuelbee;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class cartFragment extends Fragment {

    private static final String TOTAL_PRICE_KEY = "total_price";


    private String totalPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);


        TextView totalPriceTextView = rootView.findViewById(R.id.total_price);
        totalPrice = totalPriceTextView.getText().toString();


        Button proceedToPaymentButton = rootView.findViewById(R.id.proceed_to_payment);
        proceedToPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PaymentScreen.class);
                intent.putExtra(TOTAL_PRICE_KEY, totalPrice);
                startActivity(intent);
            }
        });

        return rootView;
    }
}