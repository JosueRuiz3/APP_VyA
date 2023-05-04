package com.josue.app_vya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerDetailsActivity extends AppCompatActivity {

    CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();
    BottomNavigationView bottomNavigationView;
    DetailsFragment detailsFragment = new DetailsFragment();
    AddCustomerFragment add_customerFragment = new AddCustomerFragment();
    EditFragment editFragment = new EditFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);


    }
}