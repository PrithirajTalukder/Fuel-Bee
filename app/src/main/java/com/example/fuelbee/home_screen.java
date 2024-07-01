package com.example.fuelbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.fuelbee.databinding.ActivityHomeScreenBinding; // Ensure this import is correct

public class home_screen extends AppCompatActivity {

    ActivityHomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the binding
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());

        // Set the root view of the binding as the content view
        setContentView(binding.getRoot());

        // Initialize the first fragment
        replaceFragment(new homeFragment());



        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                replaceFragment(new homeFragment());
            } else if (itemId == R.id.menu_search) {
                replaceFragment(new searchFragment());
            } else if (itemId == R.id.menu_cart) {
                replaceFragment(new cartFragment());
            } else if (itemId == R.id.menu_profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    // Helper method to replace fragments
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);  // Ensure this ID matches your XML
        fragmentTransaction.commit();
    }
}
