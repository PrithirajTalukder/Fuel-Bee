package com.example.fuelbee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    TextView tvName, tvEmail, tvAddress, tvMobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.profile_name);
        tvEmail = view.findViewById(R.id.profile_email);
        tvAddress = view.findViewById(R.id.profile_address);
        tvMobile = view.findViewById(R.id.profile_mobile);

        // Retrieve user details from SQLite and set them to TextViews
        PaymentDAO paymentDAO = new PaymentDAO(requireContext());
        paymentDAO.open();

        Payment payment = paymentDAO.getLatestPayment(); // Adjust according to your database schema

        if (payment != null) {
            tvName.setText(payment.getName());
            tvEmail.setText(payment.getEmail());
            tvAddress.setText(payment.getAddress());
            tvMobile.setText(payment.getMobile());
        }

        paymentDAO.close();

        // Logout button setup
        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout action here, e.g., navigate to Login page
                navigateToLogin();
            }
        });

        return view;
    }

    // Method to navigate to Login page
    private void navigateToLogin() {
        Intent intent = new Intent(requireContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
