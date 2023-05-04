package com.josue.app_vya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerDetailsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        bottomNavigationView  = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, customerDetailsFragment).commit();
    }
}