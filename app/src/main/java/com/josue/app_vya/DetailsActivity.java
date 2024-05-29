package com.josue.app_vya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DetailsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private DetailsFragment detailsFragment = new DetailsFragment();
    private AddCustomerFragment add_customerFragment = new AddCustomerFragment();
    private EditFragment editFragment = new EditFragment();
    private CustomerFragment customerFragment = new CustomerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bottomNavigationView  = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, customerFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.customer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, customerFragment).commit();
                        return true;
//                    case R.id.details:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, detailsFragment).commit();
//                        return true;
                    case R.id.addClient:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, add_customerFragment).commit();
                        return true;
                    case R.id.edit:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, editFragment).commit();
                        return true;
                }

                return false;
            }
        });

    }
}