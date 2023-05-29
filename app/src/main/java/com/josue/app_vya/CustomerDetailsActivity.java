package com.josue.app_vya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerDetailsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();
    private EditCustomerFragment editCustomerFragment = new EditCustomerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        bottomNavigationView  = findViewById(R.id.bottomNavigationView);

        // Obtener los datos enviados desde el intent
        Bundle extras = getIntent().getExtras();

        String id = extras.getString("id");
        String nombreCliente = extras.getString("nombre_cliente");
        String nombreProducto = extras.getString("nombre_producto");
        String cantidad = extras.getString("cantidad");
        String precioUnitario = extras.getString("precio_unitario");
        String talla = extras.getString("talla");
        String total = extras.getString("total");

        // Crear un Bundle para enviar los datos al fragmento
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("nombre_cliente", nombreCliente);
        bundle.putString("nombre_producto", nombreProducto);
        bundle.putString("cantidad", cantidad);
        bundle.putString("precio_unitario", precioUnitario);
        bundle.putString("talla", talla);
        bundle.putString("total", total);

        // Crear una instancia del fragmento y establecer los argumentos
        CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();
        customerDetailsFragment.setArguments(bundle);

        // Cargar el fragmento en el contenedor
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, customerDetailsFragment)
                .commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.detailsCustomer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, customerDetailsFragment).commit();
                        return true;
                    case R.id.editCustomer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, editCustomerFragment).commit();
                        return true;
                }

                return false;
            }
        });

    }
}