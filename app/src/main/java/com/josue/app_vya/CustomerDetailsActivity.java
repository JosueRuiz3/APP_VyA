package com.josue.app_vya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore mfirestore;
    private BottomNavigationView bottomNavigationView;
    private CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();
    private EditCustomerFragment editCustomerFragment = new EditCustomerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        bottomNavigationView  = findViewById(R.id.bottomNavigationView);
        mfirestore = FirebaseFirestore.getInstance();

        obtenerdDatos();
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

    private void obtenerdDatos(){
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

        // Crear instancias de los fragmentos y establecer los argumentos
        customerDetailsFragment = new CustomerDetailsFragment();
        customerDetailsFragment.setArguments(bundle);

        editCustomerFragment = new EditCustomerFragment();
        editCustomerFragment.setArguments(bundle);

        // Cargar el fragmento de detalles del cliente por defecto
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, customerDetailsFragment)
                .commit();
    }
}